package world.hzq.easysql.resolve.sql.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * SQL实体
 */
public abstract class SQLEntity implements Serializable {
    //数据库名
    public String database;
    //所有的子查询SQL字符串
    public List<String> subQuerySQLList = new LinkedList<>();
    //查询涉及的表名
    public List<String> tables = new ArrayList<>();
    //查询涉及到的两表之间的关系集合
    public List<String> relation = new ArrayList<>();
    //where语句条件字符串
    public String whereCondition;
    //sql语句类型
    public SQLLanguageType sqlLanguageType;

    public void setDatabase(String database) {
        this.database = database;
    }

    public List<String> getSubQuerySQLList() {
        return subQuerySQLList;
    }

    public void setSubQuerySQLList(List<String> subQuerySQLList) {
        this.subQuerySQLList = subQuerySQLList;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public List<String> getRelation() {
        return relation;
    }

    public void setRelation(List<String> relation) {
        this.relation = relation;
    }

    public String getWhereCondition() {
        return whereCondition;
    }

    public void setWhereCondition(String whereCondition) {
        this.whereCondition = whereCondition;
    }

    public String getDatabase() {
        return AbstractSQLResolver.getCurrentDatabase();
    }

    public SQLLanguageType getSqlLanguageType() {
        return sqlLanguageType;
    }

    public void setSqlLanguageType(SQLLanguageType sqlLanguageType) {
        this.sqlLanguageType = sqlLanguageType;
    }
}
