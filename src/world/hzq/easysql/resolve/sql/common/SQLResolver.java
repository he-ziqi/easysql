package world.hzq.easysql.resolve.sql.common;

import java.util.List;

/**
 * SQL解析接口
 */
public interface SQLResolver {
    //SQL是否合法
    boolean isIllegal(String sql);
    //解析SQL
    void resolve(String sql);
    //获取SQL解析结果
    <T> T getSQLEntity(Class<T> entityClass);
    SQLEntity getSQLEntity();
    //获取SQL语句
    String getSql();
    //获取执行过的所有SQL语句,默认不存储执行过的SQL语句
    List<String> getSqlList();
}
