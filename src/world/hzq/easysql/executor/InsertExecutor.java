package world.hzq.easysql.executor;

import world.hzq.easysql.config.CommonCombinationUtil;
import world.hzq.easysql.config.Config;
import world.hzq.easysql.executor.entity.SelectResultEntity;
import world.hzq.easysql.resolve.sql.common.AbstractSQLResolver;
import world.hzq.easysql.resolve.sql.common.SQLEntity;
import world.hzq.easysql.resolve.sql.entity.InsertSQLEntity;
import world.hzq.easysql.resolve.sql.entity.SelectSQLEntity;
import world.hzq.easysql.storage.IndexDirectory;
import world.hzq.easysql.storage.Page;
import world.hzq.easysql.storage.PageType;
import world.hzq.easysql.storage.struct.TableStruct;
import world.hzq.easysql.storage.type.ColumnType;
import world.hzq.easysql.storage.util.FileUtil;
import world.hzq.easysql.storage.util.IOUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InsertExecutor extends ExecutorAdapter{

    @Override
    protected void executeInsert(SQLEntity sqlEntity) {
        InsertSQLEntity insertSQLEntity = (InsertSQLEntity) sqlEntity;
        List<String> subQuerySQLList = insertSQLEntity.getSubQuerySQLList();
        List<List<String>> valueList = insertSQLEntity.getValueList();
        //如果表不存在即创建表
        String database = insertSQLEntity.getDatabase();
        String tableName = insertSQLEntity.getTable();
        //存在子查询 将子查询占位符替换为真正的结果
        if(subQuerySQLList.size() > 0){
            //子查询SQL
            List<SelectSQLEntity> subQuerySelectSQLEntityList = insertSQLEntity.getSubQuerySQLEntityList();
            List<SelectResultEntity> selectResultEntityList = new ArrayList<>();
            subQuerySelectSQLEntityList.forEach(selectEntity -> {
                Executor selectExecutor = new SelectExecutor();
                selectExecutor.execute(selectEntity);
                //获取select执行结果
                SelectResultEntity result = selectExecutor.getResult(SelectResultEntity.class);
                //将select执行结果先保存到集合中
                selectResultEntityList.add(result);
            });
            //原子Integer,避免并发产生安全问题
            AtomicInteger index = new AtomicInteger();
            //更新valueList
            valueList.forEach(values -> {
                values.forEach(value -> {
                    //当前列是子查询的结果
                    if(value != null && value.startsWith(AbstractSQLResolver.SEPARATE_SUB_QUERY)){
                        SelectResultEntity selectResultEntity = selectResultEntityList.get(index.get());
                        values.set(index.get(),selectResultEntity.getResult(String.class));
                        index.incrementAndGet();
                    }
                });
            });
        }
        //追加数据
        appendData(valueList,database,tableName);
    }

    /**
     * 追加数据
     */
    private void appendData(List<List<String>> valueList,String database,String tableName) {
        String tableStructPath = CommonCombinationUtil.getTable(database, tableName, true);
        String tableDataPath = CommonCombinationUtil.getTable(database, tableName, false);
        //要插入的文件
        File structFile = new File(tableStructPath);
        File dataFile = new File(tableDataPath);
        //表结构文件和表数据文件不存在(非法情况 禁止插入)
        if(!(FileUtil.isExists(structFile) && FileUtil.isExists(dataFile))){
            throw new RuntimeException("insert data error \n cause by : table is not exists");
        }
        //创建数据页并写入数据
        writePage(dataFile, structFile ,valueList,tableName);
    }

    /**
     * 写入数据页
     */
    private void writePage(File dataFile,File structFile,List<List<String>> valueList,String tableName){
        //先找到存储空间中的最后一页
        Page lastPage = IOUtil.readLastPage(dataFile);
        //读回表结构对象
        TableStruct tableStruct = IOUtil.readObject(structFile, TableStruct.class);
        if(lastPage == null){ //不存在数据页
            lastPage = new Page();
            int valueListSize = valueList.size();
            //设置最小记录和最大记录
            Page.Compact infimum = new Page.Compact();
            Page.Compact supremum = new Page.Compact();
            setCompact(0,valueList,infimum,tableStruct);
            lastPage.setInfimum(infimum);
            if(valueListSize == 1){
                setCompact(0,valueList,supremum,tableStruct);
            }else {
                setCompact(valueList.size() - 1,valueList,supremum,tableStruct);
            }
            lastPage.setSupremum(supremum);
            //设置fileHeader
            setFileHeader(lastPage.getFileHeader());
            //设置pageHeader和pageDirectory
            setPageHeaderAndPageDirectory(lastPage,valueList,tableStruct);
            //设置fileTrailer
            setFileTrailer(lastPage.getFileTrailer());
        }else{ //存在数据页
            setPageHeaderAndPageDirectory(lastPage,valueList,tableStruct);
        }
        //将索引顺序存储到information_schema数据库中
        /*String sysIndexTablePath = CommonCombinationUtil.getTable(Config.getSysDatabaseDirectory(), tableName, false);
        File sysIndexTableFile = new File(sysIndexTablePath);
        IndexDirectory indexDirectory = new IndexDirectory(lastPage.getInfimum().recordingHeader.getColValues().get(0),
                lastPage.getSupremum().recordingHeader.getColValues().get(0));
        IOUtil.writeObject(indexDirectory,sysIndexTableFile);*/
        //将数据页写入数据文件中
        IOUtil.writePage(lastPage,dataFile);
    }

    /**
     * 设置fileTrailer
     */
    private void setFileTrailer(Page.FileTrailer fileTrailer) {
        //暂未实现校验和与LSN
    }

    /**
     * 设置fileHeader
     */
    private void setFileHeader(Page.FileHeader fileHeader) {
        //页面类型为数据页
        fileHeader.FIL_PAGE_TYPE = PageType.FIL_PAGE_INDEX.getVal();
        fileHeader.FIL_PAGE_FILE_FLUSH_LSN = 0;
    }

    /**
     * 设置PageHeader
     */
    private void setPageHeaderAndPageDirectory(Page page, List<List<String>> valueList,TableStruct tableStruct) {
        Page.PageHeader pageHeader = page.getPageHeader();
        //设置槽并且分组
        setSlotAndGrouping(page.getPageDirectory(),pageHeader,valueList,tableStruct);
    }

    /**
     * 设置槽并且分组
     */
    private void setSlotAndGrouping(Page.PageDirectory pageDirectory,Page.PageHeader pageHeader,List<List<String>> valueList,TableStruct tableStruct) {
        List<Page.PageDirectory.Slot> slotList = pageDirectory.getSlotList();
        if (slotList.size() == 0) { //槽不存在
            //最小记录所在槽
            Page.PageDirectory.Slot infimumSlot = new Page.PageDirectory.Slot();
            //最大记录所在槽
            Page.PageDirectory.Slot supremumSlot = new Page.PageDirectory.Slot();
            int records = valueList.size();
            if(records > 2){
                records -= 1;
                pageHeader.PAGE_N_DIR_SLOTS = (short) (records / 8 + 1);
                setValueToSlot(infimumSlot,valueList,0,1,0,tableStruct);
                slotList.add(infimumSlot);
                //追加剩余的值到槽中
                appendValueToSlot(pageDirectory,pageHeader,valueList,true,tableStruct);
            }else{
                pageHeader.PAGE_N_DIR_SLOTS = 2;
                if(valueList.size() >= 1){
                    //往槽中设置值
                    setValueToSlot(infimumSlot,valueList,0,1,0,tableStruct);
                }
                if(valueList.size() == 2){
                    setValueToSlot(supremumSlot,valueList,1,2,1,tableStruct);
                }else{
                    setValueToSlot(supremumSlot,valueList,0,1,1,tableStruct);
                }
                slotList.add(infimumSlot);
                slotList.add(supremumSlot);
            }
        }else{ //槽存在
            appendValueToSlot(pageDirectory,pageHeader,valueList,false,tableStruct);
        }
    }

    /**
     * 追加值到槽中
     */
    private void appendValueToSlot(Page.PageDirectory pageDirectory, Page.PageHeader pageHeader, List<List<String>> valueList,boolean first,TableStruct tableStruct) {
        //获取最后一个槽的索引
        int slotSize = pageDirectory.getSlotList().size();
        //更新槽的大小
        pageHeader.PAGE_N_DIR_SLOTS += (short) ((valueList.size() - 1) / 8 + 1);
        //获取槽列表
        List<Page.PageDirectory.Slot> slotList = pageDirectory.getSlotList();
        int i = slotSize;
        //第一次i需要从1开始,因此多加1
        if(first){
            i++;
        }
        while(i <= pageHeader.PAGE_N_DIR_SLOTS){
            Page.PageDirectory.Slot slot = new Page.PageDirectory.Slot();
            setValueToSlot(slot,valueList,(i - 2) * 8 + 1,8 * (i - 1) + 1,i - 1,tableStruct);
            slotList.add(slot);
            i++;
        }
    }

    /**
     * 往槽中设置[start,end)的值
     * @param slot 槽
     * @param valueList 值列表
     * @param start 当前槽值的起始位置
     * @param end 当前槽值的终止位置(不包含)
     */
    private void setValueToSlot(Page.PageDirectory.Slot slot, List<List<String>> valueList, int start, int end,int slotIndex,TableStruct tableStruct) {
        List<Page.Compact> records = slot.getRecords();
        int count = 0;
        for (int i = start; i < end && i < valueList.size(); i++,count++) {
            Page.Compact compact = new Page.Compact();
            //当前槽在页面中的索引位置
            compact.recordingHeader.heapNo = slotIndex;
            if(i == start){
                //第一条记录为非叶子结点中的最小记录
                compact.recordingHeader.minRecFlag = 1;
            }else{
                compact.recordingHeader.minRecFlag = 0;
            }
            //除最后一条记录外,剩余记录的下一条记录偏移量都为1
            if(i != end - 1){
                compact.recordingHeader.nextRecord = 1;
            }else {
                compact.recordingHeader.nextRecord = 0;
            }
            setCompact(i,valueList,compact,tableStruct);
            records.add(compact);
            //当前分组中的记录数为count
            compact.recordingHeader.nOwned = count;
        }
    }

    /**
     * 设置值列表到Compact行格式中
     * @param compact 行格式
     */
    private void setCompact(int index,List<List<String>> valueList, Page.Compact compact, TableStruct tableStruct) {
        //记录头信息
        Page.RecordingHeader recordingHeader = compact.getRecordingHeader();
        recordingHeader.deletedFlag = 0;
        //默认为普通记录
        recordingHeader.recordType = 0;
        List<String> values = valueList.get(index);
        //设置值列表
        List<String> colValues = recordingHeader.getColValues();
        colValues.addAll(values);
        //计算变长字段长度列表和null值列表(正序存储)
        List<Integer> variableList = compact.getVariableList();
        List<Integer> nullList = compact.getNullList();
        //获取列的定义列表
        List<TableStruct.Column> colList = tableStruct.getColList();
        for (int i = 0;i < values.size();i++) {
            TableStruct.Column column = colList.get(i);
            if(values.get(i) == null){
                nullList.add(i + 1);
            }else if(ColumnType.VARCHAR.same(column.getDataType())){
                //是varchar变长类型才会放入变长字段列表中
                int length = values.get(i).length();
                variableList.add(length);
            }
        }
    }

}