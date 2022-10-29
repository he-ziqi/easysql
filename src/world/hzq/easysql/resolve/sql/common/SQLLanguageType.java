package world.hzq.easysql.resolve.sql.common;

public enum SQLLanguageType {
    SELECT("SELECT"),
    DELETE("DELETE"),
    INSERT("INSERT"),
    UPDATE("INSERT"),
    CREATE("CREATE"),
    ALTER("ALTER"),
    DROP("DROP"),
    TRUNCATE("TRUNCATE"),
    COMMENT("COMMENT"),
    GRANT("GRANT"),
    REVOKE("REVOKE"),
    COMMIT("COMMIT"),
    ROLLBACK("ROLLBACK"),
    SAVEPOINT("SAVEPOINT"),
    ORDER("ORDER");

    public String getTypeName() {
        return typeName;
    }

    public boolean equals(SQLLanguageType type){
        return type != null && (this.getTypeName().equals(type.getTypeName()) && this == type);
    }

    private final String typeName;

    SQLLanguageType(final String typeName){
        this.typeName = typeName;
    }
}
