package world.hzq.easysql.resolve.sql.tcl;

import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolverAdapter;

/**
 * rollback ...
 * rollback to ...
 */
public class RollbackSQLResolver extends SQLResolverAdapter {
    @Override
    public String getName() {
        return SQLLanguageType.ROLLBACK.getTypeName();
    }
}
