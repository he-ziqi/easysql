package world.hzq.easysql.resolve.sql.common;

public enum KeyWord {

    ADD("ADD"),
    ALL("ALL"),
    ALTER("ALTER"),
    ANALYZE("ANALYZE"),
    AND("AND"),
    AUTO_INCREMENT("AUTO_INCREMENT"),
    AS("AS"),
    ASC("ASC"),
    ASENSITIVE("ASENSITIVE"),
    BEFORE("BEFORE"),
    BETWEEN("BETWEEN"),
    BIGINT("BIGINT"),
    BINARY("BINARY"),
    BLOB("BLOB"),
    BOTH("BOTH"),
    BY("BY"),
    CALL("CALL"),
    CASCADE("CASCADE"),
    COMMENT("COMMENT"),
    CASE("CASE"),
    CHANGE("CHANGE"),
    CHAR("CHAR"),
    CHARACTER("CHARACTER"),
    CHECK("CHECK"),
    COLLATE("COLLATE"),
    COLUMN("COLUMN"),
    CONDITION("CONDITION"),
    CONNECTION("CONNECTION"),
    CONSTRAINT("CONSTRAINT"),
    CONTINUE("CONTINUE"),
    CONVERT("CONVERT"),
    COMMIT("COMMIT"),
    CREATE("CREATE"),
    CROSS("CROSS"),
    CURRENT_DATE("CURRENT_DATE"),
    CURRENT_TIME("CURRENT_TIME"),
    CURRENT_TIMESTAMP("CURRENT_TIMESTAMP"),
    CURRENT_USER("CURRENT_USER"),
    CURSOR("CURSOR"),
    DATABASE("DATABASE"),
    DATABASES("DATABASES"),
    DAY_HOUR("DAY_HOUR"),
    DAY_MICROSECOND("DAY_MICROSECOND"),
    DAY_MINUTE("DAY_MINUTE"),
    DAY_SECOND("DAY_SECOND"),
    DEC("DEC"),
    DECIMAL("DECIMAL"),
    DECLARE("DECLARE"),
    DEFAULT("DEFAULT"),
    DELAYED("DELAYED"),
    DELETE("DELETE"),
    DESC("DESC"),
    DESCRIBE("DESCRIBE"),
    DETERMINISTIC("DETERMINISTIC"),
    DISTINCT("DISTINCT"),
    DISTINCTROW("DISTINCTROW"),
    DIV("DIV"),
    DOUBLE("DOUBLE"),
    DROP("DROP"),
    DUAL("DUAL"),
    EACH("EACH"),
    ELSE("ELSE"),
    ELSEIF("ELSEIF"),
    ENCLOSED("ENCLOSED"),
    ESCAPED("ESCAPED"),
    EXISTS("EXISTS"),
    EXIT("EXIT"),
    EXPLAIN("EXPLAIN"),
    FALSE("FALSE"),
    FETCH("FETCH"),
    FLOAT("FLOAT"),
    FLOAT4("FLOAT4"),
    FLOAT8("FLOAT8"),
    FOR("FOR"),
    FORCE("FORCE"),
    FOREIGN("FOREIGN"),
    FROM("FROM"),
    FULLTEXT("FULLTEXT"),
    GOTO("GOTO"),
    GRANT("GRANT"),
    GROUP("GROUP"),
    HAVING("HAVING"),
    HIGH_PRIORITY("HIGH_PRIORITY"),
    HOUR_MICROSECOND("HOUR_MICROSECOND"),
    HOUR_MINUTE("HOUR_MINUTE"),
    HOUR_SECOND("HOUR_SECOND"),
    IF("IF"),
    IGNORE("IGNORE"),
    IN("IN"),
    INDEX("INDEX"),
    INFILE("INFILE"),
    INNER("INNER"),
    INOUT("INOUT"),
    INSENSITIVE("INSENSITIVE"),
    INSERT("INSERT"),
    INT("INT"),
    INT1("INT1"),
    INT2("INT2"),
    INT3("INT3"),
    INT4("INT4"),
    INT8("INT8"),
    INTEGER("INTEGER"),
    INTERVAL("INTERVAL"),
    IDENTIFIED("IDENTIFIED"),
    INTO("INTO"),
    IS("IS"),
    ITERATE("ITERATE"),
    JOIN("JOIN"),
    KEY("KEY"),
    KEYS("KEYS"),
    KILL("KILL"),
    LABEL("LABEL"),
    LEADING("LEADING"),
    LEAVE("LEAVE"),
    LEFT("LEFT"),
    LIKE("LIKE"),
    LIMIT("LIMIT"),
    LINEAR("LINEAR"),
    LINES("LINES"),
    LOAD("LOAD"),
    LOCALTIME("LOCALTIME"),
    LOCALTIMESTAMP("LOCALTIMESTAMP"),
    LOCK("LOCK"),
    LONG("LONG"),
    LONGBLOB("LONGBLOB"),
    LONGTEXT("LONGTEXT"),
    LOOP("LOOP"),
    LOW_PRIORITY("LOW_PRIORITY"),
    MATCH("MATCH"),
    MEDIUMBLOB("MEDIUMBLOB"),
    MEDIUMINT("MEDIUMINT"),
    MEDIUMTEXT("MEDIUMTEXT"),
    MIDDLEINT("MIDDLEINT"),
    MINUTE_MICROSECOND("MINUTE_MICROSECOND"),
    MINUTE_SECOND("MINUTE_SECOND"),
    MOD("MOD"),
    MODIFIES("MODIFIES"),
    NATURAL("NATURAL"),
    NOT("NOT"),
    NO_WRITE_TO_BINLOG("NO_WRITE_TO_BINLOG"),
    NULL("NULL"),
    NUMERIC("NUMERIC"),
    ON("ON"),
    OPTIMIZE("OPTIMIZE"),
    OPTION("OPTION"),
    OPTIONALLY("OPTIONALLY"),
    OR("OR"),
    ORDER("ORDER"),
    OUT("OUT"),
    OUTER("OUTER"),
    OUTFILE("OUTFILE"),
    PRECISION("PRECISION"),
    PRIMARY("PRIMARY"),
    PROCEDURE("PROCEDURE"),
    PURGE("PURGE"),
    RAID0("RAID0"),
    RANGE("RANGE"),
    READ("READ"),
    READS("READS"),
    REAL("REAL"),
    REFERENCES("REFERENCES"),
    REGEXP("REGEXP"),
    RELEASE("RELEASE"),
    RENAME("RENAME"),
    REPEAT("REPEAT"),
    REPLACE("REPLACE"),
    REQUIRE("REQUIRE"),
    RESTRICT("RESTRICT"),
    RETURN("RETURN"),
    REVOKE("REVOKE"),
    RIGHT("RIGHT"),
    ROLLBACK("ROLLBACK"),
    RLIKE("RLIKE"),
    SCHEMA("SCHEMA"),
    SCHEMAS("SCHEMAS"),
    SECOND_MICROSECOND("SECOND_MICROSECOND"),
    SELECT("SELECT"),
    SENSITIVE("SENSITIVE"),
    SEPARATOR("SEPARATOR"),
    SET("SET"),
    SAVEPOINT("SAVEPOINT"),
    SHOW("SHOW"),
    SMALLINT("SMALLINT"),
    SPATIAL("SPATIAL"),
    SPECIFIC("SPECIFIC"),
    SQL("SQL"),
    SQLEXCEPTION("SQLEXCEPTION"),
    SQLSTATE("SQLSTATE"),
    SQLWARNING("SQLWARNING"),
    SQL_BIG_RESULT("SQL_BIG_RESULT"),
    SQL_CALC_FOUND_ROWS("SQL_CALC_FOUND_ROWS"),
    SQL_SMALL_RESULT("SQL_SMALL_RESULT"),
    SSL("SSL"),
    STARTING("STARTING"),
    STRAIGHT_JOIN("STRAIGHT_JOIN"),
    TABLE("TABLE"),
    TERMINATED("TERMINATED"),
    TRUNCATE("TRUNCATE"),
    THEN("THEN"),
    TINYBLOB("TINYBLOB"),
    TINYINT("TINYINT"),
    TINYTEXT("TINYTEXT"),
    TO("TO"),
    TRAILING("TRAILING"),
    TRIGGER("TRIGGER"),
    TRUE("TRUE"),
    UNDO("UNDO"),
    UNION("UNION"),
    UNIQUE("UNIQUE"),
    UNLOCK("UNLOCK"),
    UNSIGNED("UNSIGNED"),
    UPDATE("UPDATE"),
    USAGE("USAGE"),
    USER("USER"),
    USE("USE"),
    USING("USING"),
    UTC_DATE("UTC_DATE"),
    UTC_TIME("UTC_TIME"),
    UTC_TIMESTAMP("UTC_TIMESTAMP"),
    VALUES("VALUES"),
    VIEW("VIEW"),
    VARBINARY("VARBINARY"),
    VARCHAR("VARCHAR"),
    VARCHARACTER("VARCHARACTER"),
    VARYING("VARYING"),
    WHEN("WHEN"),
    WHERE("WHERE"),
    WHILE("WHILE"),
    WITH("WITH"),
    WRITE("WRITE"),
    X509("X509"),
    XOR("XOR"),
    YEAR_MONTH("YEAR_MONTH"),
    ZEROFILL("ZEROFILL");

    private final String meaning;

    public String getMeaning(){
        return meaning;
    }

    public boolean same(String keyword){
        return keyword != null && this.getMeaning().toLowerCase().equals(keyword.toLowerCase());
    }

    KeyWord(final String meaning){
        this.meaning = meaning;
    }
}
