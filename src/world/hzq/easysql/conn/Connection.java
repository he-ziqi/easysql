package world.hzq.easysql.conn;

import world.hzq.easysql.executor.CreateExecutor;
import world.hzq.easysql.executor.Executor;
import world.hzq.easysql.executor.InsertExecutor;
import world.hzq.easysql.executor.SelectExecutor;
import world.hzq.easysql.executor.entity.SelectResultEntity;
import world.hzq.easysql.resolve.sql.common.KeyWord;
import world.hzq.easysql.resolve.sql.common.SQLResolver;
import world.hzq.easysql.resolve.sql.ddl.CreateSQLResolver;
import world.hzq.easysql.resolve.sql.dml.InsertSQLResolver;
import world.hzq.easysql.resolve.sql.dml.dql.SelectSQLResolver;
import world.hzq.easysql.resolve.sql.entity.CreateSQLEntity;
import world.hzq.easysql.resolve.sql.entity.InsertSQLEntity;
import world.hzq.easysql.resolve.sql.entity.SelectSQLEntity;
import world.hzq.easysql.resolve.sql.order.OrderResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * EasySQL
 * 因功能还未完全实现,此处采用无限循环模拟
 */
public class Connection {
    public Connection(){
    }
    public void start() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String order = null;
        //SQL语句解析器
        SQLResolver resolver = null;
        //SQL语句执行器
        Executor executor = null;
        while(!"exit".equals(order = br.readLine())){
            String createUpper = KeyWord.CREATE.getMeaning();
            String createLower = KeyWord.CREATE.getMeaning().toLowerCase();
            String selectUpper = KeyWord.SELECT.getMeaning();
            String selectLower = KeyWord.SELECT.getMeaning().toLowerCase();
            String insertUpper = KeyWord.INSERT.getMeaning();
            String insertLower = KeyWord.INSERT.getMeaning().toLowerCase();
            String useUpper = KeyWord.USE.getMeaning();
            String useLower = KeyWord.USE.getMeaning().toLowerCase();
            if (order.trim().startsWith(createLower) || order.trim().startsWith(createUpper)) {
                //create语句解析
                resolver = new CreateSQLResolver();
                executor = new CreateExecutor();
                //解析create语句
                resolver.resolve(order);
                //执行create语句
                executor.execute(resolver.getSQLEntity(CreateSQLEntity.class));
                System.out.println("OK");
            }else if(order.trim().startsWith(selectLower) || order.trim().startsWith(selectUpper)){
                //select语句解析
                resolver = new SelectSQLResolver();
                resolver.resolve(order);
                //执行select
                executor = new SelectExecutor();
                executor.execute(resolver.getSQLEntity(SelectSQLEntity.class));
                //获取结果
                SelectResultEntity resultEntity = executor.getResult(SelectResultEntity.class);
                String result = resultEntity.getResult();
                System.out.println(result);
            }else if(order.trim().startsWith(insertLower) || order.trim().startsWith(insertUpper)){
                //解析insert命令
                resolver = new InsertSQLResolver();
                resolver.resolve(order);
                //执行
                executor = new InsertExecutor();
                executor.execute(resolver.getSQLEntity(InsertSQLEntity.class));
                System.out.println("Query OK");
            }else if(order.trim().startsWith(useLower) || order.trim().startsWith(useUpper)){
                resolver = new OrderResolver();
                resolver.resolve(order);
                System.out.println("OK");
            }
        }
    }

}
