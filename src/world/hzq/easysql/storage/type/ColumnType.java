package world.hzq.easysql.storage.type;

public enum ColumnType {
    TINYINT("TINYINT"),
    SMALLINT("SMALLINT"),
    MEDIUMINT("MEDIUMINT"),
    INT("INT"),
    BIGINT("BIGINT"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    DECIMAL("DECIMAL"),

    DATE("DATE"),
    TIME("TIME"),
    YEAR("YEAR"),
    DATETIME("DATETIME"),
    TIMESTAMP("TIMESTAMP"),

    CHAR("CHAR"),
    VARCHAR("VARCHAR"),
    TINYTEXT("TINYTEXT"),
    BLOB("BLOB"),
    TEXT("TEXT"),
    MEDIUMBLOB("MEDIUMBLOB"),
    MEDIUMTEXT("MEDIUMTEXT"),
    LONGBLOB("LONGBLOB"),
    LONGTEXT("LONGTEXT");

    //类型标识
    private final String dataType;
    ColumnType(final String dataType) {
        this.dataType = dataType;
    }

    public boolean same(String dataType){
        return getDataType().toLowerCase().equals(dataType) || getDataType().equals(dataType);
    }

    public String getDataType() {
        return dataType;
    }
}
