package world.hzq.easysql.resolve.sql.common;

/**
 * SQL解析适配器
 */
public abstract class SQLResolverAdapter extends AbstractSQLResolver{
    public SQLResolverAdapter() {
    }

    @Override
    public void selectResolve(String sql) {
    }

    @Override
    public void deleteResolve(String sql) {
    }

    @Override
    public void insertResolve(String sql) {
    }

    @Override
    public void updateResolve(String sql) {
    }

    @Override
    public void createResolve(String sql) {

    }

    @Override
    public void alterResolve(String sql) {

    }

    @Override
    public void dropResolve(String sql) {

    }

    @Override
    public void truncateResolve(String sql) {

    }

    @Override
    public void commentResolve(String sql) {

    }

    @Override
    public void grantResolve(String sql) {

    }

    @Override
    public void revokeResolve(String sql) {

    }

    @Override
    public void commitResolve(String sql) {

    }

    @Override
    public void rollbackResolve(String sql) {

    }

    @Override
    public void savePointResolve(String sql) {

    }

    @Override
    public void orderResolve(String sql) {
    }

    @Override
    protected int fromResolve(SQLEntity sqlEntity, String[] items, int start) {
        return super.fromResolve(sqlEntity, items, start);
    }

    @Override
    public void setSubQueryToSQLEntityAndResolve(String storageType, String indexStr, SQLEntity parentSQLEntity) {

    }

    @Override
    protected void whereResolve(SQLEntity sqlEntity, String[] items, int start) {
        super.whereResolve(sqlEntity, items, start);
    }

    @Override
    public void orderResolve(SQLEntity sqlEntity, String[] items, int start) {

    }

    @Override
    public void groupResolve(SQLEntity sqlEntity, String[] items, int start) {

    }
}
