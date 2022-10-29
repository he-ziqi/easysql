package world.hzq.easysql.resolve.sql.common;

public interface SQLInfo extends SQLType{
    SQLEntity getSQLEntity();
    void setSQLEntity(SQLEntity sqlEntity);
}
