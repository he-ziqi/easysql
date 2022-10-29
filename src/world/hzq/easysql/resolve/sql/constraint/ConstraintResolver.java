package world.hzq.easysql.resolve.sql.constraint;

import world.hzq.easysql.resolve.sql.common.AbstractSQLResolver;
import world.hzq.easysql.resolve.sql.common.KeyWord;
import world.hzq.easysql.storage.index.IndexType;
import world.hzq.easysql.storage.struct.TableStruct;

import java.io.Serializable;
import java.util.*;

/**
 * create table 语句 约束解析器
 */
public class ConstraintResolver implements Serializable {
    private String tableName;
    //表约束集合
    private final Set<AbstractConstraint> constraintList = new HashSet<>();
    //列数据集合
    private final List<TableStruct.Column> colList;
    public ConstraintResolver(int colSize){
        //初始化列数据集合
        colList = new ArrayList<>(colSize);
    }
    /**
     * 解析列定义
     * @param columnList 列定义字符串
     */
    public void resolveColumn(String[] columnList){
        for (String column : columnList) {
            resolveColumn(column);
        }
        //将获取单独约束列定义设置到列定义中
        constraintList.forEach(constraint->{
            List<String> constraintColumnList = constraint.getConstraintColumnList();
            for (TableStruct.Column column : colList) {
                String name = column.getName();
                for (String columnName : constraintColumnList) {
                    if (name.equals(columnName)) {
                        column.getConstraintList().add(constraint);
                    }
                }
            }
        });
        //将在列上直接定义的约束存入约束集合中
        for (TableStruct.Column column : colList) {
            constraintList.addAll(column.getConstraintList());
        }
    }

    //解析表定义的行字符串
    private void resolveColumn(String columnStr){
        //当前列定义字符串为单独的约束定义行
        AbstractConstraint constraint = null;
        if(columnStr.startsWith(KeyWord.PRIMARY.getMeaning()) || columnStr.startsWith(KeyWord.PRIMARY.getMeaning().toLowerCase())){
            //primary key 约束定义
            constraint = new PrimaryKeyConstraint();
            constraint.resolve(columnStr);
        }else if(columnStr.startsWith(KeyWord.FOREIGN.getMeaning()) || columnStr.startsWith(KeyWord.FOREIGN.getMeaning().toLowerCase())){
            //foreign key 约束定义
            constraint = new ForeignKeyConstraint();
            constraint.resolve(columnStr);
        }else if(columnStr.startsWith(KeyWord.CHECK.getMeaning()) || columnStr.startsWith(KeyWord.CHECK.getMeaning().toLowerCase())){
            //check 约束定义
            constraint = new CheckConstraint();
            constraint.resolve(columnStr);
        }else if(columnStr.startsWith(KeyWord.UNIQUE.getMeaning()) || columnStr.startsWith(KeyWord.UNIQUE.getMeaning().toLowerCase())){
            //unique 约束定义
            constraint = new UniqueConstraint();
            constraint.resolve(columnStr);
        }else{
            resolveColumnConstraint(columnStr);
        }
        if(constraint != null){
            constraint.setTableName(tableName);
            //索引名称(类名+uuid前3位)
            constraint.setConstraintName(constraint.getClass().getSimpleName() + UUID.randomUUID().toString().substring(0,4));
            constraintList.add(constraint);
        }
    }

    //解析定义在列后的约束
    private void resolveColumnConstraint(String columnStr){
        String[] items = columnStr.split(AbstractSQLResolver.SEPARATE);
        TableStruct.Column column = new TableStruct.Column();
        //设置列名
        column.setName(items[0]);
        //检查数据类型是否存在
        if(column.isDataType(items[1])){
            //设置数据类型
            column.setDataType(items[1]);
        }else{
            throw new RuntimeException("create table error \n cause by : column type error near " + items[1]);
        }
        StringBuilder sb = new StringBuilder();
        if(items.length > 2 && "(".equals(items[2])){
            for(int i = 3;i < items.length;i++){
                if(!")".equals(items[i])){
                    sb.append(items[i]);
                }else {
                    break;
                }
            }
        }else{
            sb.append(TableStruct.Column.DEFAULT_DATA_LENGTH);
        }
        //设置数据长度
        column.setDataLength(sb.toString());
        //寻找列定义字符串中包含哪些约束,如果包含则进行解析
        if(columnStr.contains(ConstraintType.PRIMARY_KEY.getMeaning().toUpperCase()) ||
                columnStr.contains(ConstraintType.PRIMARY_KEY.getMeaning().toLowerCase())){
            primaryKeyResolve(column,items);
        }
        if(columnStr.contains(ConstraintType.AUTO_INCREMENT.getMeaning().toUpperCase()) ||
                columnStr.contains(ConstraintType.AUTO_INCREMENT.getMeaning().toLowerCase())) {
            autoIncrementResolve(column,items);
        }
        if(columnStr.contains(ConstraintType.NOT_NULL.getMeaning().toUpperCase()) ||
                columnStr.contains(ConstraintType.NOT_NULL.getMeaning().toLowerCase())){
            notNullResolve(column,items);
        }
        if(columnStr.contains(ConstraintType.DEFAULT.getMeaning().toUpperCase()) ||
                columnStr.contains(ConstraintType.DEFAULT.getMeaning().toLowerCase())){
            DefaultResolve(column,columnStr);
        }
        if(columnStr.contains(ConstraintType.ZEROFILL.getMeaning().toUpperCase()) ||
                columnStr.contains(ConstraintType.ZEROFILL.getMeaning().toLowerCase())){
            zerofillResolve(column,items);
        }
        if(columnStr.contains(ConstraintType.UNIQUE.getMeaning().toUpperCase()) ||
                columnStr.contains(ConstraintType.UNIQUE.getMeaning().toLowerCase())){
            uniqueResolve(column,items);
        }
        if(columnStr.contains(ConstraintType.CHECK.getMeaning().toUpperCase()) ||
                columnStr.contains(ConstraintType.CHECK.getMeaning().toLowerCase())){
            checkResolve(column,columnStr);
        }
        colList.add(column);
    }

    //check约束解析
    private void checkResolve(TableStruct.Column column, String columnStr) {
        //找到check约束开始的索引位置
        int start = !columnStr.contains(ConstraintType.CHECK.getMeaning().toUpperCase())
                ? columnStr.indexOf(ConstraintType.CHECK.getMeaning().toLowerCase())
                : columnStr.indexOf(ConstraintType.CHECK.getMeaning().toUpperCase());
        start += 5;
        char[] chs = columnStr.toCharArray();
        if(chs[start] != '('){
            throw new RuntimeException("create table error \n cause by : check constraint lack '('");
        }
        StringBuilder content = new StringBuilder();
        for(int i = start + 1;i < chs.length;i++){
            if(chs[i] != ')'){
                content.append(chs[i]);
            }
        }
        CheckConstraint checkConstraint = new CheckConstraint(content.toString());
        column.getConstraintList().add(checkConstraint);
        //索引名称(类名+uuid前3位)
        checkConstraint.setConstraintName(checkConstraint.getClass().getSimpleName()
                + UUID.randomUUID().toString().substring(0,4));
        checkConstraint.setTableName(tableName);
        checkConstraint.setConstraintContent("default = " + content.toString());
    }

    //唯一性约束解析
    private void uniqueResolve(TableStruct.Column column, String[] items) {
        UniqueConstraint uniqueConstraint = new UniqueConstraint(items[0]);
        //设置当前列为非聚簇索引
        column.setIndexType(IndexType.NON_CLUSTERED);
        column.getConstraintList().add(uniqueConstraint);
        //索引名称(类名+uuid前3位)
        uniqueConstraint.setConstraintName(uniqueConstraint.getClass().getSimpleName()
                + UUID.randomUUID().toString().substring(0,4));
        uniqueConstraint.setTableName(tableName);
        uniqueConstraint.setConstraintContent(ConstraintType.UNIQUE.getMeaning());
    }

    //自增长约束解析
    private void zerofillResolve(TableStruct.Column column, String[] items) {
        CommonConstraint zerofillConstraint = new CommonConstraint(ConstraintType.ZEROFILL,items[0]);
        column.getConstraintList().add(zerofillConstraint);//索引名称(类名+uuid前3位)
        zerofillConstraint.setConstraintName(zerofillConstraint.getClass().getSimpleName()
                + UUID.randomUUID().toString().substring(0,4));
        zerofillConstraint.setTableName(tableName);
        zerofillConstraint.setConstraintContent(ConstraintType.ZEROFILL.getMeaning());
    }

    //默认约束解析
    private void DefaultResolve(TableStruct.Column column, String columnStr) {
        String[] items = columnStr.split(AbstractSQLResolver.SEPARATE);
        char[] chs = columnStr.toCharArray();
        StringBuilder str = new StringBuilder();
        int start = chs.length - 2;
        while(chs[start] != '\''){
            str.append(chs[start--]);
        }
        String defaultContent = str.reverse().toString();
        DefaultConstraint defaultConstraint = new DefaultConstraint(defaultContent,items[0]);
        column.getConstraintList().add(defaultConstraint);
        //索引名称(类名+uuid前3位)
        defaultConstraint.setConstraintName(defaultConstraint.getClass().getSimpleName()
                + UUID.randomUUID().toString().substring(0,4));
        defaultConstraint.setTableName(tableName);
        defaultConstraint.setConstraintContent("default = " + defaultContent);
    }

    //非空约束解析
    private void notNullResolve(TableStruct.Column column, String[] items) {
        CommonConstraint notNullConstraint = new CommonConstraint(ConstraintType.NOT_NULL,items[0]);
        column.getConstraintList().add(notNullConstraint);
        //索引名称(类名+uuid前3位)
        notNullConstraint.setConstraintName(notNullConstraint.getClass().getSimpleName()
                + UUID.randomUUID().toString().substring(0,4));
        notNullConstraint.setTableName(tableName);
        notNullConstraint.setConstraintContent(ConstraintType.NOT_NULL.getMeaning());
    }

    //主键解析
    private void primaryKeyResolve(TableStruct.Column column, String[] items){
        PrimaryKeyConstraint primaryKeyConstraint = new PrimaryKeyConstraint(items[0]);
        //设置当前列索引类型为聚簇索引
        column.setIndexType(IndexType.CLUSTERED);
        //设置当前列相关联的约束
        column.getConstraintList().add(primaryKeyConstraint);
        //索引名称(类名+uuid前3位)
        primaryKeyConstraint.setConstraintName(primaryKeyConstraint.getClass().getSimpleName()
                + UUID.randomUUID().toString().substring(0,4));
        primaryKeyConstraint.setTableName(tableName);
        primaryKeyConstraint.setConstraintContent(ConstraintType.PRIMARY_KEY.getMeaning());
    }
    //自增长约束解析
    private void autoIncrementResolve(TableStruct.Column column, String[] items){
        CommonConstraint autoIncrementConstraint = new CommonConstraint(ConstraintType.AUTO_INCREMENT,items[0]);
        column.getConstraintList().add(autoIncrementConstraint);
        //索引名称(类名+uuid前3位)
        autoIncrementConstraint.setConstraintName(autoIncrementConstraint.getClass().getSimpleName()
                + UUID.randomUUID().toString().substring(0,4));
        autoIncrementConstraint.setTableName(tableName);
        autoIncrementConstraint.setConstraintContent(ConstraintType.AUTO_INCREMENT.getMeaning());
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Set<AbstractConstraint> getConstraintList() {
        return constraintList;
    }

    public List<TableStruct.Column> getColList() {
        return colList;
    }
}
