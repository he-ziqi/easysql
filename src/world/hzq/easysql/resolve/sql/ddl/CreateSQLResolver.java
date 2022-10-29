package world.hzq.easysql.resolve.sql.ddl;

import world.hzq.easysql.resolve.sql.common.*;
import world.hzq.easysql.resolve.sql.dml.dql.SelectSQLResolver;
import world.hzq.easysql.resolve.sql.entity.CreateSQLEntity;
import world.hzq.easysql.resolve.sql.entity.SelectSQLEntity;
import world.hzq.easysql.storage.struct.TableStruct;

import java.util.Arrays;

/**
 * 创建语句解析
 */
public class CreateSQLResolver extends SQLResolverAdapter {
    /**
     * 五种情况
     * 1、create table...
     * 2、create database...
     * 3、create index... (create index 索引名 on 表名(字段列表))
     * 4、create user...(create user 'username'@'host' identified by 'password')
     * 5、create view...(create view 视图名 as select语句)
     */
    @Override
    public void createResolve(String sql) {
        CreateSQLEntity sqlEntity = getSQLEntity(CreateSQLEntity.class);
        String[] items = sql.replaceAll(" +",SEPARATE).split(SEPARATE);
        if(items.length < 4){
            throw new RuntimeException("SQL resolve error \ncause by : sql is illegal");
        }
        if(KeyWord.TABLE.same(items[1])){
            sqlEntity.setType(KeyWord.TABLE.getMeaning());
            tableResolve(sqlEntity,sql);
        }else if(KeyWord.DATABASE.same(items[1])){
            sqlEntity.setType(KeyWord.DATABASE.getMeaning());
            databaseResolve(sqlEntity,items);
        }else if(KeyWord.INDEX.same(items[1])){
            sqlEntity.setType(KeyWord.INDEX.getMeaning());
            indexResolve(sqlEntity,sql);
        }else if(KeyWord.USER.same(items[1])){
            sqlEntity.setType(KeyWord.USER.getMeaning());
            userResolve(sqlEntity,sql);
        }else if(KeyWord.VIEW.same(items[1])){
            sqlEntity.setType(KeyWord.VIEW.getMeaning());
            viewResolve(sqlEntity,items);
        }else{
            throw new RuntimeException("SQL resolve error \ncause by : sql struct error the index 2 is " + items[1]);
        }
    }

    /**
     * 创建视图解析
     */
    private void viewResolve(CreateSQLEntity sqlEntity, String[] items) {
        sqlEntity.setViewName(items[2]);
        if(!KeyWord.AS.same(items[3])){
            throw new RuntimeException("SQL resolve error \ncause by : sql struct error the index 3 is " + items[3] + " should as");
        }
        StringBuilder querySql = new StringBuilder();
        for (int i = 4; i < items.length; i++) {
            querySql.append(items[i]).append(" ");
        }
        SQLResolver resolver = new SelectSQLResolver();
        resolver.resolve(querySql.toString());
        SelectSQLEntity selectSQLEntity = resolver.getSQLEntity(SelectSQLEntity.class);
        sqlEntity.setSelectSQLEntity(selectSQLEntity);
    }

    /**
     * 创建用户解析
     */
    private void userResolve(CreateSQLEntity sqlEntity, String sql) {
        String[] items = sql.replaceAll("(( )*@( )*)", " @ ")
                .replaceAll(" +", SEPARATE).split(SEPARATE);
        if(items.length != 9 || !"@".equals(items[3]) || !KeyWord.IDENTIFIED.same(items[5]) || !KeyWord.BY.same(items[6])){
            throw new RuntimeException("SQL resolve error \n cause by : sql struct error maybe near @ or identified by");
        }
        sqlEntity.setUsername(items[2].substring(1,items[2].length() - 1));
        sqlEntity.setHost(items[4].substring(1,items[4].length() - 1));
        sqlEntity.setPassword(items[7].substring(1,items[7].length() - 1));
    }

    /**
     * 创建索引解析
     */
    private void indexResolve(CreateSQLEntity sqlEntity, String sql) {
        String[] items = sql.replaceAll("(( )*\\(( )*)"," ( ")
                .replaceAll("(( )*\\)( )*)"," ) ")
                .replaceAll("(( )*,( )*)"," , ")
                .replaceAll(" +",SEPARATE)
                .split(SEPARATE);
        if(items.length < 8 || !KeyWord.ON.same(items[3]) || !"(".equals(items[5])){
            throw new RuntimeException("SQL resolve error \ncause by : sql struct error maybe near on or '('");
        }
        sqlEntity.setIndexName(items[2]);
        sqlEntity.getTables().add(items[4]);
        StringBuilder fieldsStr = new StringBuilder();
        for (int i = 6; i < items.length; i++) {
            if(!")".equals(items[i])){
                fieldsStr.append(items[i]);
            }else{
                break;
            }
        }
        sqlEntity.getFieldList().addAll(Arrays.asList(fieldsStr.toString().split(",")));
    }

    /**
     * 创建数据库语句解析
     */
    private void databaseResolve(CreateSQLEntity sqlEntity, String[] items) {
        sqlEntity.setDatabase(items[3]);
    }

    /**
     * 创建表解析
     * create table t_user(
     *      id bigint primary key auto_increment,
     *      name varchar(20) not null,
     *      ...
     *      foreign key(name) references t_tab(id)
     * );
     */
    private void tableResolve(CreateSQLEntity sqlEntity, String sql) {
        String[] items = sql.replaceAll("(( )*\\(( )*)"," ( ")
                .replaceAll("(( )*\\)( )*)"," ) ")
                .replaceAll("(( )*,( )*)"," , ")
                .replaceAll(" +",SEPARATE)
                .split(SEPARATE);
        if(!("(".equals(items[3]) && ")".equals(items[items.length - 2]))){
            throw new RuntimeException("sql resolve error \n cause by : table struct should start with '(' end with ')' ");
        }
        //代表左括号出现次数,出现一次右括号就-1 减为0时表示列定义已匹配完成
        int cnt = 1;
        //开始索引位置
        int start = 4;
        //拼接列字符串
        StringBuilder colStr = new StringBuilder();
        while(cnt > 0){
            if("(".equals(items[start])){
                cnt++;
            }else if(")".equals(items[start])){
                cnt--;
            }
            if(cnt > 0){
                //当前为,
                if(",".equals(items[start])){
                    //如果当前是列定义末尾则将,替换为列结束分隔符
                    if(KeyWord.KEY.same(items[start - 1]) || KeyWord.AUTO_INCREMENT.same(items[start - 1]) ||
                        KeyWord.NULL.same(items[start - 1]) || ")".equals(items[start - 1]) ||
                            (items[start - 1].startsWith("'") && items[start - 1].endsWith("'"))){
                        //上一个是auto_increment、null、)、key或以'开头'结尾的
                       items[start] = SEPARATE_COLUMN;
                       colStr.delete(colStr.length() - 1,colStr.length());
                    }
                }
                colStr.append(items[start]);
                //不以,结尾且不是列结束标记则添加分隔符(当前列结束)
                if(!items[start].endsWith(",") && !SEPARATE_COLUMN.equals(items[start])){
                    colStr.append(SEPARATE);
                }
                start++;
            }
        }
        //得到列定义数组
        String[] colList = colStr.toString().split(SEPARATE_COLUMN);
        TableStruct tableStruct = new TableStruct(colList.length);
        tableStruct.setTableName(items[2]);
        //解析所有的列定义
        tableStruct.resolve(colList);
        //设置表结构
        sqlEntity.setTableStruct(tableStruct);
    }

    @Override
    public String getName() {
        return SQLLanguageType.CREATE.getTypeName();
    }
}
