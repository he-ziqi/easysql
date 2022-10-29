package world.hzq.easysql.resolve.sql.entity;

import world.hzq.easysql.resolve.sql.common.SQLEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeleteSQLEntity extends SQLEntity implements Serializable {
    public static transient final String strandSQL = "delete ? from ? where ? = ?";
    //delete后的子查询存储实体
    private final List<SelectSQLEntity> deleteSQLEntities = new ArrayList<>();
    //where后的子查询存储实体
    private final List<SelectSQLEntity> whereSQLEntities = new ArrayList<>();

    public List<SelectSQLEntity> getDeleteSQLEntities() {
        return deleteSQLEntities;
    }

    public List<SelectSQLEntity> getWhereSQLEntities() {
        return whereSQLEntities;
    }

    @Override
    public String toString() {
        return "DeleteSQLEntity{" +
                "whereCondition='" + whereCondition + '\'' +
                ", deleteSQLEntities=" + deleteSQLEntities +
                ", whereSQLEntities=" + whereSQLEntities +
                ", subQuerySQLList=" + subQuerySQLList +
                ", tables=" + tables +
                ", relation=" + relation +
                '}';
    }
}
