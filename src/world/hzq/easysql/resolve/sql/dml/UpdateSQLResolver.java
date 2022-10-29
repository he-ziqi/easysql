package world.hzq.easysql.resolve.sql.dml;

import world.hzq.easysql.resolve.sql.common.*;
import world.hzq.easysql.resolve.sql.dml.dql.SelectSQLResolver;
import world.hzq.easysql.resolve.sql.entity.SelectSQLEntity;
import world.hzq.easysql.resolve.sql.entity.UpdateSQLEntity;

public class UpdateSQLResolver extends SQLResolverAdapter {

    @Override
    public void updateResolve(String sql) {
        UpdateSQLEntity sqlEntity = getSQLEntity(UpdateSQLEntity.class);
        sql = replaceSubQuery(sqlEntity,sql);
        String[] items = sql.replaceAll(" +, +",",")
                .replaceAll(" +,",",")
                .replaceAll(", +",",")
                .replaceAll(" +",SEPARATE).trim().toLowerCase().split(SEPARATE);
        if(items.length < 6){
            throw new RuntimeException("SQL resolve error \ncause by : sql is illegal");
        }
        //解析开始索引位置
        int start = 0;
        //update后表名解析
        start = tableResolve(sqlEntity,items,start);
        //set子句解析
        start = setResolve(sqlEntity,items,start);
        //存在where子句(正常结束时start位置为length - 1)
        if(start < items.length - 1){
            //where子句解析
            whereResolve(sqlEntity,items,start);
        }
    }

    /**
     * where子句解析
     */
    private void whereResolve(UpdateSQLEntity sqlEntity, String[] items, int start) {
        StringBuilder whereCondition = new StringBuilder();
        for (int i = start + 1; i < items.length; i++) {
            //只有";"一种结尾方式
            if(!";".equals(items[i])){
                whereCondition.append(items[i]);
            }
            //存在子查询
            if (items[i].startsWith("#")) {
                setSubQueryToSQLEntityAndResolve(KeyWord.WHERE.getMeaning(),items[i],sqlEntity);
            }
        }
        sqlEntity.setWhereCondition(whereCondition.toString());
    }

    /**
     *
     * set子句解析
     * @return 返回where子句或结束的索引位置
     */
    private int setResolve(UpdateSQLEntity sqlEntity, String[] items, int start) {
        StringBuilder setStr = new StringBuilder();
        for (int i = start + 1; i < items.length; i++) {
            //有两种结尾情况";"或where
            if(!KeyWord.WHERE.same(items[i]) && !";".equals(items[i])){
                setStr.append(items[i]);
            }else{
                start = i;
                break;
            }
        }
        //如果没有跳出循环或者不以";"或where结尾则为非法SQL
        if(start >= items.length || (!KeyWord.WHERE.same(items[start]) && !";".equals(items[start]))){
            throw new RuntimeException("SQL resolve error \ncause by : sql is illegal");
        }
        sqlEntity.setSetCondition(setStr.toString());
        return start;
    }

    /**
     * update列表解析
     * @return set解析开始位置
     */
    private int tableResolve(UpdateSQLEntity sqlEntity, String[] items, int start) {
        StringBuilder tables = new StringBuilder();
        for (int i = start + 1; i < items.length; i++) {
            if(!KeyWord.SET.same(items[i])){
                tables.append(items[i]).append(SEPARATE);
            }else{
                start = i;
                break;
            }
        }
        if(start >= items.length || !KeyWord.SET.same(items[start])){
            throw new RuntimeException("SQL resolve error \n cause by : sql is illegal");
        }
        String[] updateList = tables.toString().split(",");
        for (String s : updateList) {
            //出现子查询
            if (s.startsWith(SEPARATE_SUB_QUERY)) {
                setSubQueryToSQLEntityAndResolve(KeyWord.UPDATE.getMeaning(),s,sqlEntity);
            }
            //将表名存入到UpdateSQLEntity中
            sqlEntity.getTables().add(s);
        }
        //返回set解析的开始索引位置
        return start;
    }

    /**
     * 获取子查询语句并解析后设置到当前SQL存储实体中
     */
    private void setSubQueryToSQLEntityAndResolve(String storageType,String indexStr,UpdateSQLEntity parentSQLEntity){
        int index = Integer.parseInt(String.valueOf(indexStr.charAt(1)));
        //注意此时子查询语句必须从父SQLEntity中获取,子类中还未存储(解析算法导致将所有子查询解析到父类中了)
        String subQuerySQL = getSQLEntity(UpdateSQLEntity.class).getSubQuerySQLList().get(index);
        //设置子查询的父存储实体为当前SQLEntity 只有当前实体中的SubQuerySQLList才存储了子查询的字符串
        SQLResolver selectSQLResolve = new SelectSQLResolver(parentSQLEntity);
        selectSQLResolve.resolve(subQuerySQL);
        SelectSQLEntity selectSQLEntity = selectSQLResolve.getSQLEntity(SelectSQLEntity.class);
        if(KeyWord.UPDATE.same(storageType)){
            parentSQLEntity.getUpdateSQLEntities().add(selectSQLEntity);
        }else if(KeyWord.WHERE.same(storageType)){
            parentSQLEntity.getWhereSQLEntities().add(selectSQLEntity);
        }else{
            throw new RuntimeException("SQL resolve error \ncause by : sub query sql storage error : " + storageType);
        }
    }

    @Override
    public String getName() {
        return SQLLanguageType.UPDATE.getTypeName();
    }

}