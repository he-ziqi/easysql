package world.hzq.easysql.resolve.sql.ddl;

import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolverAdapter;

public class DropSQLResolver extends SQLResolverAdapter {
    @Override
    public void dropResolve(String sql) {

    }

    @Override
    public String getName() {
        return SQLLanguageType.DROP.getTypeName();
    }
}
