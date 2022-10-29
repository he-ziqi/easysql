package world.hzq.easysql.resolve.sql.entity;

import world.hzq.easysql.resolve.sql.common.SQLEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InsertSQLEntity extends SQLEntity implements Serializable {
    private static transient final String strandSQL = "insert into ?(?,?) values(?,?)";
    //表名
    private String table;
    //要插入的字段列表
    private List<String> insertList = new ArrayList<>();
    //值列表
    List<List<String>> valueList = new ArrayList<>();
    //子查询存储实体
    List<SelectSQLEntity> subQuerySQLEntityList = new ArrayList<>();

    public void setTable(String table) {
        this.table = table;
    }

    public void setInsertList(List<String> insertList) {
        this.insertList = insertList;
    }

    public void setValueList(List<String> valueList) {
        int len = valueList.size() / getInsertList().size();
        for (int i = 0; i < len; i++) {
            this.valueList.add(new ArrayList<>());
        }
        int index = 0;
        for(int i = 0;i < valueList.size();i++){
            this.valueList.get(index).add(valueList.get(i));
            if((i + 1) % getInsertList().size() == 0){
                index++;
            }
        }
    }

    public String getTable() {
        return table;
    }

    public List<String> getInsertList() {
        return insertList;
    }

    public List<List<String>> getValueList() {
        return valueList;
    }

    public List<SelectSQLEntity> getSubQuerySQLEntityList() {
        return subQuerySQLEntityList;
    }

    public void setSubQuerySQLEntityList(List<SelectSQLEntity> subQuerySQLEntityList) {
        this.subQuerySQLEntityList = subQuerySQLEntityList;
    }

    @Override
    public String toString() {
        return "InsertSQLEntity{" +
                "table='" + table + '\'' +
                ", insertList=" + insertList +
                ", valueList=" + valueList +
                ", subQuerySQLEntityList=" + subQuerySQLEntityList +
                '}';
    }
}
