package world.hzq.easysql.resolve.sql.entity;

import world.hzq.easysql.resolve.sql.common.SQLEntity;
import world.hzq.easysql.storage.struct.TableStruct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建语句存储实体
 */
public class CreateSQLEntity extends SQLEntity implements Serializable {
    //创建语句类型 table、view、index
    public String type;
    //索引名
    public String indexName;
    //字段列表
    public List<String> fieldList = new ArrayList<>();
    //用户名
    public String username;
    //登录权限
    public String host;
    //密码
    public transient String password;
    //视图名
    public String viewName;
    //视图创建用的查询语句实体
    public SelectSQLEntity selectSQLEntity;
    //表结构
    public TableStruct tableStruct;

    public TableStruct getTableStruct() {
        return tableStruct;
    }

    public void setTableStruct(TableStruct tableStruct) {
        this.tableStruct = tableStruct;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public SelectSQLEntity getSelectSQLEntity() {
        return selectSQLEntity;
    }

    public void setSelectSQLEntity(SelectSQLEntity selectSQLEntity) {
        this.selectSQLEntity = selectSQLEntity;
    }

    @Override
    public String toString() {
        return "CreateSQLEntity{" +
                "type='" + type + '\'' +
                ", indexName='" + indexName + '\'' +
                ", fieldList=" + fieldList +
                ", username='" + username + '\'' +
                ", host='" + host + '\'' +
                ", password='" + password + '\'' +
                ", viewName='" + viewName + '\'' +
                ", selectSQLEntity=" + selectSQLEntity +
                ", tableStruct=" + tableStruct +
                '}';
    }
}
