package world.hzq.easysql.test;

import world.hzq.easysql.storage.Page;
import world.hzq.easysql.storage.ReadPageEntity;
import world.hzq.easysql.storage.util.ByteUtil;
import world.hzq.easysql.storage.util.IOUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        /*String selectSQL = "select s.id,s.name,s.age,sc.score,c.name,c.id from " +
                "student as s left join score as sc on s.id = sc.id " +
                "right join course as c on sc.cid = c.id " +
                "where s.age in (select ss.age from student as ss where age > 20 and name like 'z%') " +
                "group by s.id " +
                "having sc.score > 80 " +
                "order by sc.score desc;";
        SQLResolver resolver = new SelectSQLResolver();
        resolver.resolve(selectSQL);
        System.out.println("select 语句解析结果：\n" + resolver.getSQLEntity(SelectSQLEntity.class));

        String insertSQL = "insert into t_user(id,name,age) values ((select id from t_user where id = (select id from t_ids where name = 'zs') )  , '张三',28),(1,'ls',20);";
        resolver = new InsertSQLResolver();
        resolver.resolve(insertSQL);

        System.out.println("insert 语句解析结果：\n" + resolver.getSQLEntity(InsertSQLEntity.class));
        String deleteSQL = "delete   from user as u left join student as s on u.id = s.uid left join " +
                "( select (select * from t_student as tss left join t_sex as tsx on tss.id = tsx.id where age > 10) " +
                "from t_score as ttt where age > 20 ) " +
                "as ts on s.id = ts.id " +
                "where s.id = (select id from student as s right join score as sc on s.id = sc.id);";
        resolver = new DeleteSQLResolver();
        resolver.resolve(deleteSQL);
        System.out.println("delete 语句解析结果：\n" + resolver.getSQLEntity(DeleteSQLEntity.class));

        resolver = new UpdateSQLResolver();
        String updateSQL = " update t_user as t,(select s.name,sc.score,s.id from t_stu as s left join t_score as sc on s.id = sc.id where s.age > 20) set id = 1,name = 'ls'" +
                " where name = (select (select name from t where name = '1') from t where id = 2);";
        resolver.resolve(updateSQL);
        System.out.println("update 语句解析结果：\n" + resolver.getSQLEntity(UpdateSQLEntity.class));

        resolver = new CreateSQLResolver();
        String createSQL = "   create table t_user ( id bigint primary key auto_increment," +
                "     name varchar(20) default 'ssscca'  ," +
                "     foreign key(name , id) references t_tab(id,name)" +
                ");";
        resolver.resolve(createSQL);
        System.out.println("create table 语句解析结果：\n" + resolver.getSQLEntity(CreateSQLEntity.class));

        String createViewSQL = "create view viewName as " + selectSQL;
        resolver.resolve(createViewSQL);
        System.out.println("create view 语句解析结果：\n" + resolver.getSQLEntity(CreateSQLEntity.class));*/
        /*System.out.println("压缩结果：");
        Page page1 = new Page();
        byte[] bytes = ByteUtil.toByteArray(page1,true);
        System.out.println(bytes.length);
        //page1.freeSpace = Page.PAGE_SIZE - bytes.length;

        bytes = ByteUtil.toByteArray(page1);
        System.out.println(bytes.length);

        System.out.println("非压缩结果：");
        //int t = page1.freeSpace;
        bytes = ByteUtil.toByteArray(page1, false);
        System.out.println(bytes.length);
        //page1.freeSpace -= t;
        bytes = ByteUtil.toByteArray(page1,false);
        System.out.println(bytes.length);

        File file = new File("test.ibd");
        ReadPageEntity read3 = IOUtil.read(2, file);
        Page page3r = ByteUtil.toObject(read3.getBytes(), read3.getOffset(), Page.class);
        System.out.println(page3r);*/
        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
        String s = stringList.get(1);
        stringList.set(1,"abc");
        stringList.forEach(System.out::println);
    }
}
