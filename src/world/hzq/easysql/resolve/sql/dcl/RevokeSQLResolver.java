package world.hzq.easysql.resolve.sql.dcl;

import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolverAdapter;

/**
 * 收回已授予的权限
 */
public class RevokeSQLResolver extends SQLResolverAdapter {
    @Override
    public void revokeResolve(String sql) {

    }

    @Override
    public String getName() {
        return SQLLanguageType.REVOKE.getTypeName();
    }
}
