package world.hzq.easysql.resolve.sql;

import world.hzq.easysql.resolve.sql.common.SQLType;
import world.hzq.easysql.resolve.sql.common.Types;

/**
 * JDBC类型枚举
 */
public enum JDBCType implements SQLType {
    TINYINT(Types.TINYINT),
    SMALLINT(Types.SMALLINT),
    INTEGER(Types.INTEGER),
    BIGINT(Types.BIGINT),
    FLOAT(Types.FLOAT),
    DOUBLE(Types.DOUBLE),
    NUMERIC(Types.NUMERIC),
    DECIMAL(Types.DECIMAL),
    CHAR(Types.CHAR),
    VARCHAR(Types.VARCHAR),
    DATE(Types.DATE),
    TIME(Types.TIME),
    TIMESTAMP(Types.TIMESTAMP),
    BINARY(Types.BINARY),
    VARBINARY(Types.VARBINARY),
    NULL(Types.NULL),
    DISTINCT(Types.DISTINCT),
    STRUCT(Types.STRUCT),
    ARRAY(Types.ARRAY),
    BOOLEAN(Types.BOOLEAN),
    ROWID(Types.ROWID),
    NVARCHAR(Types.NVARCHAR),
    NCHAR(Types.NCHAR);

    //类型标识
    private Integer type;
    JDBCType(final int type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name();
    }

}
