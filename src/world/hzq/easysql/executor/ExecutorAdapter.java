package world.hzq.easysql.executor;

import world.hzq.easysql.resolve.sql.common.SQLEntity;

/**
 * 执行器适配器
 */
public abstract class ExecutorAdapter extends AbstractExecutor{

    @Override
    protected void executeSelect(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeCommit(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeComment(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeSavePoint(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeRollback(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeRevoke(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeGrant(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeDrop(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeAlter(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeOrder(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeUpdate(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeInsert(SQLEntity sqlEntity) {

    }

    @Override
    protected void executeCreate(SQLEntity sqlEntity) {

    }
}