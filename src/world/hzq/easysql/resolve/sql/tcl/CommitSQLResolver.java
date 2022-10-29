package world.hzq.easysql.resolve.sql.tcl;

import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolverAdapter;

public class CommitSQLResolver extends SQLResolverAdapter {
    @Override
    public void commitResolve(String sql) {

    }

    @Override
    public String getName() {
        return SQLLanguageType.COMMIT.getTypeName();
    }
}
