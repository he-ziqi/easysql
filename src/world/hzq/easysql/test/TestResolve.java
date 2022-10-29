package world.hzq.easysql.test;

import world.hzq.easysql.resolve.sql.common.SQLResolver;
import world.hzq.easysql.resolve.sql.entity.DeleteSQLEntity;
import world.hzq.easysql.resolve.sql.dml.DeleteSQLResolver;


public class TestResolve {
    public static void main(String[] args) {
        /*String sql = "select s.id,s.name, c.id , c.name from student as s " +
                "inner join course as c on s.id = c.id left join score as sc on c.id = sc.id " +
                "where s.age between 20 and               100  and s.id = '101' or s.no in (1,2,3) group by s.id having c.name != 'cname' order by s.id;           ";
        *///String sql = "select id,name,age from t_user as t left join t_stu as s on t.id = s.id right join t_score as sc on s.id = sc.id where id = 1;";
        /*String sql = "select (   select name from t_user    ),age from t_stu as t inner join (select ( select name from user where name like '%ja%') from score as s1 left join curse as c1 on s1.id = c1.id where age > 1) as sc on t.id = sc.id where id in ( 1,2,   3) and name like (select name from t_stu) group by age having age > ( select age from t_admin where id = '103' or id = '104');";
        SQLResolver resolve = new SelectSQLResolver();
        resolve.resolve(sql);
        SQLEntity entity = resolve.getSQLEntity();
        System.out.println(entity.getTables().toString());*/
        /*for (int i = 0; i < entity.getSelectSubQueryEntities().size(); i++) {
            System.out.println(entity.getSelectSubQueryEntities().get(i));
        }
        System.out.println("============================================================================================");
        for (int i = 0; i < entity.getFormSubQueryEntities().size(); i++) {
            System.out.println(entity.getFormSubQueryEntities().get(i));
        }
        System.out.println("============================================================================================");
        for (int i = 0; i < entity.getWhereSubQueryEntities().size(); i++) {
            System.out.println(entity.getWhereSubQueryEntities().get(i));
        }
        */
        //System.out.println(entity.getHavingSubQueryEntities());
        /*String sql = "insert into t_user(id,name,age) values ((select id from t_user where id = (select id from t_ids where name = 'zs') )  , '张三',28),(1,'ls',20);";
        InsertSQLResolve insertSQLResolve = new InsertSQLResolve();
        insertSQLResolve.resolve(sql);
        System.out.println(insertSQLResolve.getSQLEntity());*/
        /*String sql = " update t_user as t,(select s.name,sc.score,s.id from t_stu as s left join t_score as sc on s.id = sc.id where s.age > 20) set id = 1,name = 'ls'" +
                " where name = (select (select name from t where name = '1') from t where id = 2);";
        UpdateSQLResolve resolve = new UpdateSQLResolve();
        resolve.resolve(sql);
        //System.out.println(resolve.getSQLEntity());
        resolve.getSQLEntity().getWhereSQLEntities().forEach(s-> System.out.println(s));
        System.out.println(resolve.getSql());*/
        String sql2 = "delete   from user as u left join student as s on u.id = s.uid left join " +
                "( select (select * from t_student as tss left join t_sex as tsx on tss.id = tsx.id where age > 10) from t_score as ttt where age > 20 ) " +
                "as ts on s.id = ts.id " +
                "where s.id = (select id from student as s right join score as sc on s.id = sc.id);";
        SQLResolver resolver = new DeleteSQLResolver();
        resolver.resolve(sql2);
        DeleteSQLEntity entity2 = (DeleteSQLEntity) resolver.getSQLEntity();
        System.out.println(entity2.getTables().toString());
        entity2.getSubQuerySQLList().forEach(System.out::println);
        //System.out.println(entity2.getDeleteSQLEntities().get(0).getSubQuerySQLList().size());
        entity2.getWhereSQLEntities().forEach(s->{
            //System.out.println(s.getTables().toString());
            System.out.println(s.getTables().toString());
        });
        //System.out.println(entity2.getWhereSQLEntities().size());

        /*String sql = "select a.*,b.* from t1 as a left join t2 as b on a.id = b.id where a.id = 1;";
        SQLResolver resolver = new SelectSQLResolver();
        resolver.resolve(sql);
        System.out.println(resolver.getSQLEntity());*/
    }


}
