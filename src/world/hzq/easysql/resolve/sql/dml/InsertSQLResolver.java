package world.hzq.easysql.resolve.sql.dml;

import world.hzq.easysql.resolve.sql.common.*;
import world.hzq.easysql.resolve.sql.dml.dql.SelectSQLResolver;
import world.hzq.easysql.resolve.sql.entity.InsertSQLEntity;
import world.hzq.easysql.resolve.sql.entity.SelectSQLEntity;

import java.util.Arrays;

/**
 * 插入语句解析器
 */
public class InsertSQLResolver extends SQLResolverAdapter{

    @Override
    public void insertResolve(String sql) {
        //获取父类中创建的SQLEntity
        InsertSQLEntity sqlEntity = getSQLEntity(InsertSQLEntity.class);
        //替换子查询字符串
        sql = replaceSubQuery(sqlEntity, sql);
        String[] items = sql.replaceAll(" +, +",",")
                .replaceAll(" +,",",")
                .replaceAll(", +",",")
                .replaceAll("\\(( )*"," (")
                .replaceAll(" +\\)",")")
                .replaceAll("\\)( )*,( )*\\(","),(")
                .replaceAll("\\(", "")
                .replaceAll("\\)","")
                .replaceAll(" +",SEPARATE).trim().toLowerCase().split(SEPARATE);
        if(items.length < 5){
            throw new RuntimeException("SQL resolve error \ncause by : sql is illegal");
        }
        //存在子查询
        if(sqlEntity.getSubQuerySQLList().size() > 0){
            sqlEntity.getSubQuerySQLList().forEach(selectSQL -> {
                //解析当前查询语句并存储
                //将子查询的父实体设置为当前存储实体
                SQLResolver selectSQLResolve = new SelectSQLResolver(sqlEntity);
                selectSQLResolve.resolve(selectSQL);
                sqlEntity.getSubQuerySQLEntityList().add(selectSQLResolve.getSQLEntity(SelectSQLEntity.class));
            });
        }
        sqlEntity.setTable(items[2]);
        sqlEntity.setInsertList(Arrays.asList(items[3].split(",")));
        sqlEntity.setValueList(Arrays.asList(items[5].split(",")));
    }

    @Override
    public String getName() {
        return SQLLanguageType.INSERT.getTypeName();
    }

}
