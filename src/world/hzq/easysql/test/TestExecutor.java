package world.hzq.easysql.test;

import world.hzq.easysql.executor.CreateExecutor;
import world.hzq.easysql.executor.Executor;
import world.hzq.easysql.executor.InsertExecutor;
import world.hzq.easysql.executor.SelectExecutor;
import world.hzq.easysql.executor.entity.SelectResultEntity;
import world.hzq.easysql.resolve.sql.common.SQLResolver;
import world.hzq.easysql.resolve.sql.ddl.CreateSQLResolver;
import world.hzq.easysql.resolve.sql.dml.InsertSQLResolver;
import world.hzq.easysql.resolve.sql.dml.dql.SelectSQLResolver;
import world.hzq.easysql.resolve.sql.entity.CreateSQLEntity;
import world.hzq.easysql.resolve.sql.entity.InsertSQLEntity;
import world.hzq.easysql.resolve.sql.entity.SelectSQLEntity;
import world.hzq.easysql.resolve.sql.order.OrderResolver;

public class TestExecutor {
    /*static {
        insertTest();
    }*/
    public static void main(String[] args) {
        selectTest();
    }
    public static void insertTest(){
        String order = "use esaysql;";
        SQLResolver resolver = new OrderResolver();
        resolver.resolve(order);
        String insertSQL = "insert into t_user(id,name,age) values(1,'zs',20),(2,'ls',25),(3,'wz',30)" +
                ",(4,'赵六',39),(5,'王五',15),(6,'张三',26),(7,'李四',45);";
        resolver = new InsertSQLResolver();
        resolver.resolve(insertSQL);
        Executor executor = new InsertExecutor();
        executor.execute(resolver.getSQLEntity(InsertSQLEntity.class));
    }
    public static void selectTest(){
        String order = "use esaysql;";
        SQLResolver resolver = new OrderResolver();
        resolver.resolve(order);
        String sql = "select id,name,age from t_user where age > 20;";
        resolver = new SelectSQLResolver();
        resolver.resolve(sql);
        Executor executor = new SelectExecutor();
        executor.execute(resolver.getSQLEntity(SelectSQLEntity.class));
        SelectResultEntity result = executor.getResult(SelectResultEntity.class);
        String res = result.getResult();
        System.out.println(res);
    }
}
