package world.hzq.easysql.executor;

import world.hzq.easysql.executor.entity.ResultEntity;
import world.hzq.easysql.resolve.sql.common.SQLEntity;
import world.hzq.easysql.resolve.sql.common.SQLResolver;

/**
 * 执行器接口
 */
public interface Executor {
    //执行SQL
    void execute(SQLResolver resolver);
    void execute(SQLEntity sqlEntity);
    //获取执行结果
    ResultEntity getResult();
    <T> T getResult(Class<T> resultEntityClass);
}
