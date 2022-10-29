package world.hzq.easysql.resolve.sql.order;

import world.hzq.easysql.resolve.function.BranchHandle;
import world.hzq.easysql.resolve.sql.common.KeyWord;
import world.hzq.easysql.resolve.sql.common.OrderType;
import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolverAdapter;
import world.hzq.easysql.resolve.sql.entity.OrderEntity;

/**
 * 常用命令解析
 */
public class OrderResolver extends SQLResolverAdapter {
    private static final String SHOW_TABLE = "show tables;";
    private static final String SHOW_DATABASE = "show databases;";
    /**
     * 命令解析
     * use databaseName;
     * show tables;
     * show databases;
     * desc tableName;
     */
    @Override
    public void orderResolve(String sql) {
        OrderEntity sqlEntity = getSQLEntity(OrderEntity.class);
        sql = sql.replaceAll(" +", " ").replaceAll("(( )*;)",";");
        String finalSql = sql;
        BranchHandle.handle(SHOW_TABLE.equals(sql) || SHOW_TABLE.toUpperCase().equals(sql))
                .execute(()->{
                    //show table处理
                    sqlEntity.setOrderType(OrderType.SHOW_TABLES);
                },()->{
                    BranchHandle.handle(SHOW_DATABASE.equals(finalSql) || SHOW_DATABASE.toUpperCase().equals(finalSql))
                            .execute(()->{
                                //show database处理
                                sqlEntity.setOrderType(OrderType.SHOW_DATABASES);
                            },()->{
                                BranchHandle.handle(KeyWord.DESC.same(finalSql.substring(0,4)))
                                        .execute(()->{
                                            //desc处理
                                            sqlEntity.setOrderType(OrderType.DESC_TABLE);
                                            sqlEntity.getTables().add(finalSql.substring(5,finalSql.length() - 1));
                                        },()->{
                                            BranchHandle.handle(KeyWord.USE.same(finalSql.substring(0,3)))
                                                    .execute(()->{
                                                        //use处理
                                                        sqlEntity.setOrderType(OrderType.USE_TABLE);
                                                        String database = finalSql.substring(4, finalSql.length() - 1);
                                                        sqlEntity.setDatabase(database);
                                                        setCurrentDatabase(database);
                                                    },()->{
                                                        throw new RuntimeException("order resolve fail \n cause by : order struct error");
                                                    });
                                        });
                            });
                });
    }


    @Override
    public String getName() {
        return SQLLanguageType.ORDER.getTypeName();
    }

}
