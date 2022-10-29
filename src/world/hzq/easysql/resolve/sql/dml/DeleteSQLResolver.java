package world.hzq.easysql.resolve.sql.dml;

import world.hzq.easysql.resolve.sql.common.KeyWord;
import world.hzq.easysql.resolve.sql.common.*;
import world.hzq.easysql.resolve.sql.dml.dql.SelectSQLResolver;
import world.hzq.easysql.resolve.sql.entity.DeleteSQLEntity;
import world.hzq.easysql.resolve.sql.entity.SelectSQLEntity;

import java.util.List;

public class DeleteSQLResolver extends SQLResolverAdapter {

    @Override
    public void deleteResolve(String sql) {
        DeleteSQLEntity sqlEntity = getSQLEntity(DeleteSQLEntity.class);
        sql = replaceSubQuery(sqlEntity,sql);
        String[] items = sql.replaceAll(" +, +",",")
                .replaceAll(" +,",",")
                .replaceAll(", +",",")
                .replaceAll(" +",SEPARATE).trim().toLowerCase().split(SEPARATE);
        if(items.length < 4){
            throw new RuntimeException("SQL resolve error \ncause by : sql is illegal");
        }
        //默认为delete form ...的形式
        int start = 0;
        if(KeyWord.FROM.same(items[2])){
            start = 1;
        }else if(!KeyWord.FROM.same(items[1])){
            throw new RuntimeException("SQL resolve error \ncause by : sql struct error");
        }
        //from子句解析
        start = fromResolve(sqlEntity, items, start);
        //where子句解析
        whereResolve(sqlEntity,items,start);
        //这里选择全部转换为大写 后面在统计子查询个数时可以用KeyWord枚举来判断边界条件
        //统计并且解析子查询
        cntAndSetSubQueryEntityAndResolve(sqlEntity,sql.toUpperCase());
    }

    /**
     * 统计并且解析子查询
     */
    private void cntAndSetSubQueryEntityAndResolve(DeleteSQLEntity sqlEntity, String sql) {
        if(sqlEntity.getSubQuerySQLList().size() > 0) {
            char[] chs = sql.toCharArray();
            int fromCnt = 0;
            int endIdx = sql.indexOf(KeyWord.WHERE.getMeaning());
            for (int i = endIdx; i > 0; i--) {
                if(SEPARATE_SUB_QUERY.equals(String.valueOf(chs[i]))) {
                    fromCnt = Integer.parseInt(String.valueOf(chs[i + 1])) + 1;
                    break;
                }
            }
            List<String> subQuerySQLList = sqlEntity.getSubQuerySQLList();
            for (int i = 0; i < subQuerySQLList.size(); i++) {
                String subQuerySQL = subQuerySQLList.get(i);
                SQLResolver resolver = new SelectSQLResolver(sqlEntity);
                resolver.resolve(subQuerySQL);
                if(i < fromCnt){
                    sqlEntity.getDeleteSQLEntities().add(resolver.getSQLEntity(SelectSQLEntity.class));
                }else{
                    sqlEntity.getWhereSQLEntities().add(resolver.getSQLEntity(SelectSQLEntity.class));
                }
            }
        }
    }

    @Override
    public String getName() {
        return SQLLanguageType.DELETE.getTypeName();
    }

}
