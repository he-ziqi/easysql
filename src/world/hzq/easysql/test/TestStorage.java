package world.hzq.easysql.test;

import world.hzq.easysql.executor.Executor;
import world.hzq.easysql.executor.InsertExecutor;
import world.hzq.easysql.resolve.sql.common.SQLResolver;
import world.hzq.easysql.resolve.sql.dml.InsertSQLResolver;
import world.hzq.easysql.resolve.sql.order.OrderResolver;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestStorage {
    public static void main(String[] args) throws FileNotFoundException {
        Executor executor = new InsertExecutor();
        SQLResolver resolver1 = new OrderResolver();
        resolver1.resolve("use mysql;");
        SQLResolver resolver = new InsertSQLResolver();
        String sql = "insert into t_user(id,name,age) values(1,'zs',20);";
        resolver.resolve(sql);
        executor.execute(resolver);
    }
}
