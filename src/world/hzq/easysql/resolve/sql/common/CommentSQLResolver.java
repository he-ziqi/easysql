package world.hzq.easysql.resolve.sql.common;

/**
 * 注释
 */
public class CommentSQLResolver extends SQLResolverAdapter {
    @Override
    public void commentResolve(String sql) {

    }

    @Override
    public String getName() {
        return SQLLanguageType.COMMENT.getTypeName();
    }
}
