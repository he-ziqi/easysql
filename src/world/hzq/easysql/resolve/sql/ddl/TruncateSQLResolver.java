package world.hzq.easysql.resolve.sql.ddl;

import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolverAdapter;

/**
 * 删除表
 */
public class TruncateSQLResolver extends SQLResolverAdapter {
    @Override
    public void truncateResolve(String sql) {

    }

    @Override
    public String getName() {
        return SQLLanguageType.TRUNCATE.getTypeName();
    }
}
