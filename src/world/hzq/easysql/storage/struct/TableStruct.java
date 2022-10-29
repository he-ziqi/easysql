package world.hzq.easysql.storage.struct;

import world.hzq.easysql.resolve.sql.constraint.*;
import world.hzq.easysql.storage.index.IndexType;
import world.hzq.easysql.storage.type.ColumnType;

import java.io.Serializable;
import java.util.*;

/**
 * 表结构定义
 */
public class TableStruct implements Serializable {
    //表名
    private String tableName;
    //列数据集合
    private final List<Column> colList;
    //表约束集合
    private final Set<AbstractConstraint> constraintList = new HashSet<>();
    //约束解析器
    private final ConstraintResolver constraintResolver;
    public TableStruct(int colSize){
        colList = new ArrayList<>(colSize);
        constraintResolver = new ConstraintResolver(colSize);
    }

    //解析所有的列
    public void resolve(String[] columnList){
        constraintResolver.setTableName(getTableName());
        constraintResolver.resolveColumn(columnList);
        colList.addAll(constraintResolver.getColList());
        constraintList.addAll(constraintResolver.getConstraintList());
    }

    //列数据定义
    public static class Column implements Serializable{
        public static final int DEFAULT_DATA_LENGTH = 10;
        //列名称
        private String name;
        //数据类型
        private String dataType;
        //数据长度
        private String dataLength;
        //索引类型
        private IndexType indexType = IndexType.NOT_INDEX;
        //当前字段的约束
        private final List<AbstractConstraint> constraintList = new ArrayList<>();

        /**
         * 判断当前数据类型是否存在
         */
        public boolean isDataType(String type) {
            ColumnType[] types = ColumnType.values();
            for (ColumnType columnType : types) {
                if(columnType.same(type)){
                    return true;
                }
            }
            return false;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getDataLength() {
            return dataLength;
        }

        public void setDataLength(String dataLength) {
            this.dataLength = dataLength;
        }

        public IndexType getIndexType() {
            return indexType;
        }

        public void setIndexType(IndexType indexType) {
            this.indexType = indexType;
        }

        public List<AbstractConstraint> getConstraintList() {
            return constraintList;
        }

        @Override
        public String toString() {
            return "Column{" +
                    "name='" + name + '\'' +
                    ", dataType='" + dataType + '\'' +
                    ", dataLength='" + dataLength + '\'' +
                    ", indexType=" + indexType +
                    ", constraintList=" + constraintList +
                    '}';
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColList() {
        return colList;
    }

    public Set<AbstractConstraint> getConstraintList() {
        return constraintList;
    }

    @Override
    public String toString() {
        return "TableStruct{" +
                "tableName='" + tableName + '\'' +
                ", colList=" + colList +
                '}';
    }
}
