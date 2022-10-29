package world.hzq.easysql.executor.entity;

/**
 * 执行结果
 */
public abstract class ResultEntity {
    public abstract <T> T getResult(Class<T> targetClass);
    public abstract String getResult();
}
