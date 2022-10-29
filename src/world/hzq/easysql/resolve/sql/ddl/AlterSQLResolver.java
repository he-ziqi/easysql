package world.hzq.easysql.resolve.sql.ddl;

import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolverAdapter;

/**
 * 修改表结构
 */
public class AlterSQLResolver extends SQLResolverAdapter {
    @Override
    public void alterResolve(String sql) {

    }

    @Override
    public String getName() {
        return SQLLanguageType.ALTER.getTypeName();
    }
}
