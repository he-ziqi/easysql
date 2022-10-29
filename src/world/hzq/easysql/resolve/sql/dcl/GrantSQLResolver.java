package world.hzq.easysql.resolve.sql.dcl;

import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolverAdapter;

/**
 * 授权
 */
public class GrantSQLResolver extends SQLResolverAdapter {
    @Override
    public void grantResolve(String sql) {

    }

    @Override
    public String getName() {
        return SQLLanguageType.GRANT.getTypeName();
    }
}
