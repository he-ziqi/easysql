package world.hzq.easysql.resolve.sql.entity;

import world.hzq.easysql.resolve.sql.common.SQLEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateSQLEntity extends SQLEntity implements Serializable {
    private static transient final String strandSQL = "update ? set ? = ? where ? = ?";
    //更新字符串
    private String setCondition;
    //update后的表名子查询存储实体
    private final List<SelectSQLEntity> updateSQLEntities = new ArrayList<>();
    //where后的子查询存储实体
    private final List<SelectSQLEntity> whereSQLEntities = new ArrayList<>();

    public String getSetCondition() {
        return setCondition;
    }

    public void setSetCondition(String setCondition) {
        this.setCondition = setCondition;
    }

    public List<SelectSQLEntity> getUpdateSQLEntities() {
        return updateSQLEntities;
    }

    public List<SelectSQLEntity> getWhereSQLEntities() {
        return whereSQLEntities;
    }

    @Override
    public String toString() {
        return "UpdateSQLEntity{" +
                "setCondition='" + setCondition + '\'' +
                ", whereCondition='" + whereCondition + '\'' +
                ", tables=" + tables +
                ", updateSQLEntities=" + updateSQLEntities +
                ", whereSQLEntities=" + whereSQLEntities +
                ", subQuerySQLList=" + subQuerySQLList +
                '}';
    }
}
