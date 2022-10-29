package world.hzq.easysql.executor;

import world.hzq.easysql.config.CommonCombinationUtil;
import world.hzq.easysql.executor.entity.SelectResultEntity;
import world.hzq.easysql.resolve.sql.common.AbstractSQLResolver;
import world.hzq.easysql.resolve.sql.common.SQLEntity;
import world.hzq.easysql.resolve.sql.entity.SelectSQLEntity;
import world.hzq.easysql.storage.Page;
import world.hzq.easysql.storage.struct.TableStruct;
import world.hzq.easysql.storage.util.IOUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 查询执行器
 */
public class SelectExecutor extends ExecutorAdapter{
    @Override
    protected void executeSelect(SQLEntity sqlEntity) {
        SelectSQLEntity selectSQLEntity = (SelectSQLEntity) sqlEntity;
        //查询的数据库名
        String database = selectSQLEntity.getDatabase();
        List<String> subQuerySQLList = selectSQLEntity.getSubQuerySQLList();

        //存在子查询
        if(subQuerySQLList.size() > 0){
            //深度优先搜索
            selectSQLEntity.getSelectSubQueryEntities().forEach(subQueryEntry -> {
                Executor executor = new SelectExecutor();
                executor.execute(subQueryEntry);
                //sql执行结果
                SelectResultEntity result = executor.getResult(SelectResultEntity.class);
            });
        }else{ //不存在子查询
            //查询的表
            SelectResultEntity selectResultEntity = select(selectSQLEntity);
            setResultEntity(selectResultEntity);
        }
    }

    private SelectResultEntity select(SelectSQLEntity selectSQLEntity) {
        SelectResultEntity res = null;
        List<String> tables = selectSQLEntity.getTables();
        String database = selectSQLEntity.getDatabase();
        String table = selectSQLEntity.getTables().get(0);
        table = table.replaceAll(AbstractSQLResolver.SEPARATE,"");
        String tablePath = CommonCombinationUtil.getTable(database, table, false);
        String tableStructPath = CommonCombinationUtil.getTable(database, table, true);
        File tableFile = new File(tablePath);
        File tableStructFile = new File(tableStructPath);
        if (tables.size() == 1) { //单表查询
            //单表查询
            res = singletonTableSelect(selectSQLEntity,tableFile,tableStructFile);
        }else if(tables.size() > 1){ //多表查询
            res = multitonTableSelect(selectSQLEntity,tableFile,tableStructFile);
        }
        return res;
    }

    /**
     * 单表查询
     */
    private SelectResultEntity singletonTableSelect(SelectSQLEntity selectSQLEntity,File tableFile,File tableStructFile) {
        SelectResultEntity res = new SelectResultEntity();
        //查询列表
        List<String> queryFields = selectSQLEntity.getQueryFields();
        String[] queryFiledArray = queryFields.get(0).split(",");
        //查询sql类型
        int selectType = selectSQLEntity.getSelectType();
        if(selectType == 1){
            selectType1(res,selectSQLEntity,tableFile,tableStructFile);
        }else if(selectType == 2){
            selectType2(res,selectSQLEntity,tableFile,tableStructFile);
        }
        return res;
    }

    /**
     * 暂时通过全表扫描的方式实现where查询(只实现了单查询,例如：age > 20)
     * 但实际上应该通过二分查找页目录进行索引查询
     * 因时间有限,目前做粗糙处理!!!!!!!!!!!!!!!!!!!!!!
     */
    private void selectType2(SelectResultEntity res, SelectSQLEntity selectSQLEntity,File tableFile,File tableStructFile) {
        List<Page> pages = IOUtil.readAllPage(tableFile);
        //whereCondition样例： age~>~20
        String whereCondition = selectSQLEntity.getWhereCondition();
        String[] whereConditionArray = whereCondition.split(AbstractSQLResolver.SEPARATE);
        TableStruct tableStruct = IOUtil.readObject(tableStructFile, TableStruct.class);
        String[] queryFiledArray = selectSQLEntity.getQueryFields().get(0).split(",");
        res.setQueryList(selectSQLEntity.getQueryFields());
        //找到要查询的列和条件列的索引
        List<Integer> queryIndex = new ArrayList<>();
        int index = 0;
        int conditionIndex = -1;
        List<TableStruct.Column> colList = tableStruct.getColList();
        for (int i = 0; i < colList.size() && index < queryFiledArray.length; i++) {
            if(colList.get(i).getName().equals(queryFiledArray[index])){
                queryIndex.add(i);
                index++;
            }
            if(colList.get(i).getName().equals(whereConditionArray[0])){
                conditionIndex = i;
            }
        }
        for (Page page : pages) {
            List<Page.PageDirectory.Slot> slotList = page.getPageDirectory().getSlotList();
            for (Page.PageDirectory.Slot slot : slotList) {
                for (Page.Compact record : slot.getRecords()) {
                    List<String> colValues = record.getRecordingHeader().getColValues();
                    //通过条件列索引过滤record
                    String conditionColValue = colValues.get(conditionIndex); //获取条件列的值
                    //获取条件值
                    String conditionValue = whereConditionArray[2];
                    //根据条件运算符进行运算,目前只支持> < =
                    boolean ac = false; //是否通过条件校验
                    switch (whereConditionArray[1]){
                        case ">":
                            if(Integer.parseInt(conditionColValue) > Integer.parseInt(conditionValue)){
                               ac = true;
                            }
                            break;
                        case "<":
                            if(Integer.parseInt(conditionColValue) < Integer.parseInt(conditionValue)){
                                ac = true;
                            }
                            break;
                        case "=":
                            if(Integer.parseInt(conditionColValue) == Integer.parseInt(conditionValue)){
                                ac = true;
                            }
                            break;
                    }
                    //通过校验,则将这个记录添加到结果实体行中
                    if(ac){
                        List<String> temp = new ArrayList<>();
                        //通过查询列表的索引,将通过ac校验的查询结果存储
                        for (int i = 0; i < queryIndex.size(); i++) {
                            index = queryIndex.get(i);
                            String colValue = colValues.get(index);
                            temp.add(colValue);
                        }
                        SelectResultEntity.Row row = new SelectResultEntity.Row();
                        row.setColList(temp);
                        res.addRow(row);
                    }
                }
            }
        }
    }

    private void selectType1(SelectResultEntity res,SelectSQLEntity selectSQLEntity,File tableFile,File tableStructFile) {
        List<String> queryFields = selectSQLEntity.getQueryFields();
        String[] queryFiledArray = queryFields.get(0).split(",");
        //读取数据页
        List<Page> pages = IOUtil.readAllPage(tableFile);
        //读取表结构
        TableStruct tableStruct = IOUtil.readObject(tableStructFile, TableStruct.class);
        //设置查询列表
        res.setQueryList(queryFields);
        for (Page page : pages) {
            List<Page.PageDirectory.Slot> slotList = page.getPageDirectory().getSlotList();
            for (Page.PageDirectory.Slot slot : slotList) {
                List<Page.Compact> records = slot.getRecords();
                //查询所有
                if(queryFiledArray.length == 1 && queryFields.get(0).equals("*")){
                    for (Page.Compact record : records) {
                        List<String> colValues = record.getRecordingHeader().getColValues();
                        SelectResultEntity.Row row = new SelectResultEntity.Row();
                        row.setColList(colValues);
                        res.addRow(row);
                    }
                }else{
                    //找到要查询的列
                    List<Integer> queryIndex = new ArrayList<>();
                    int index = 0;
                    List<TableStruct.Column> colList = tableStruct.getColList();
                    for (int i = 0; i < colList.size() && index < queryFiledArray.length; i++) {
                        if(colList.get(i).getName().equals(queryFiledArray[index])){
                            queryIndex.add(i);
                            index++;
                        }
                    }
                    for (Page.Compact record : records) {
                        List<String> colValues = record.getRecordingHeader().getColValues();
                        List<String> temp = new ArrayList<>(queryIndex.size());
                        for (int i = 0; i < queryIndex.size(); i++) {
                            index = queryIndex.get(i);
                            String colValue = colValues.get(index);
                            temp.add(colValue);
                        }
                        SelectResultEntity.Row row = new SelectResultEntity.Row();
                        row.setColList(temp);
                        res.addRow(row);
                    }
                }
            }
        }
    }

    /**
     * 多表查询(未完成)
     */
    private SelectResultEntity multitonTableSelect(SelectSQLEntity selectSQLEntity,File tableFile,File tableStructFile) {
        SelectResultEntity res = null;
        return res;
    }
}
