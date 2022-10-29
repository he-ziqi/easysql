package world.hzq.easysql.resolve.sql.tcl;

import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolverAdapter;

public class SavePointSQLResolver extends SQLResolverAdapter {
    @Override
    public String getName() {
        return SQLLanguageType.SAVEPOINT.getTypeName();
    }
}
