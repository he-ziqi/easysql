package world.hzq.easysql.test;

import world.hzq.easysql.resolve.sql.common.SQLResolver;
import world.hzq.easysql.resolve.sql.ddl.CreateSQLResolver;
import world.hzq.easysql.resolve.sql.entity.CreateSQLEntity;
import world.hzq.easysql.resolve.sql.entity.OrderEntity;
import world.hzq.easysql.resolve.sql.order.OrderResolver;

public class TestResolve2 {
    public static void main(String[] args) {
        String sql = "   create table t_user ( id bigint primary key auto_increment," +
                "     name varchar(20) default 'ssscca'  ," +
                "     foreign key(name , id) references t_tab(id,name)" +
                ");";
        SQLResolver resolver = new CreateSQLResolver();
        resolver.resolve(sql);
        System.out.println(resolver.getSQLEntity(CreateSQLEntity.class)/*.getTableStruct().getTableName()*/);
        /*String sql = "select s.id,s.name, c.id , c.name from student as s " +
                "inner join course as c on s.id = c.id left join score as sc on c.id = sc.id " +
                "where s.age between 20 and               100  and s.id = '101' or s.no in (1,2,3) group by s.id having c.name != 'cname' order by s.id;           ";
        SQLResolver resolver = new SelectSQLResolver();
        resolver.resolve(sql);
        SelectSQLEntity entity = resolver.getSQLEntity(SelectSQLEntity.class);
        System.out.println(entity);*/
        /*String sql = "create index indexName on tableName (     ?    ,   ?    ,     ?    ) ;";
        SQLResolver resolver = new CreateSQLResolver();
        resolver.resolve(sql);
        System.out.println(resolver.getSQLEntity());*/
        /*String sql = " update t_user as t,(select s.name,sc.score,s.id from t_stu as s left join t_score as sc on s.id = sc.id where s.age > 20) set id = 1,name = 'ls'" +
                " where name = (select (select name from t where name = '1') from t where id = 2);";
        SQLResolver resolve = new UpdateSQLResolver();
        resolve.resolve(sql);
        System.out.println(resolve.getSQLEntity());*/
        /*String sql1 = "  show   databases   ;  ";
        String sql2 = "show tables;   ";
        String sql3 = "use   mysql   ;";
        String sql4 = " desc  student; ";
        SQLResolver resolver = new OrderResolver();
        resolver.resolve(sql1);
        System.out.println(resolver.getSQLEntity(OrderEntity.class));
        resolver.resolve(sql2);
        System.out.println(resolver.getSQLEntity(OrderEntity.class));
        resolver.resolve(sql3);
        System.out.println(resolver.getSQLEntity(OrderEntity.class));
        resolver.resolve(sql4);
        System.out.println(resolver.getSQLEntity(OrderEntity.class));*/
    }
}
