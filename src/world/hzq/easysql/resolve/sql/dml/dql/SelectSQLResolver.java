package world.hzq.easysql.resolve.sql.dml.dql;

import world.hzq.easysql.resolve.sql.common.*;
import world.hzq.easysql.resolve.sql.entity.SelectSQLEntity;

import java.util.*;

/**
 * 查询语句解析器
 */
public class SelectSQLResolver extends SQLResolverAdapter {
    //子查询字符串的父存储实体
    private SQLEntity subQuerySuperEntity;

    public SelectSQLResolver(SQLEntity subQuerySuperEntity) {
        this.subQuerySuperEntity = subQuerySuperEntity;
    }

    public SelectSQLResolver() {}

    @Override
    public void selectResolve(String sql) {
        //获取父类中创建的SQLEntity
        SelectSQLEntity sqlEntity = getSQLEntity(SelectSQLEntity.class);
        resolve(sqlEntity,sql);
    }

    /**
     *
     * @param sqlEntity 存储解析信息的SQLEntity实体
     * @param sql 要解析的SQL
     */
    private void resolve(SelectSQLEntity sqlEntity,String sql){
        sql = replaceSubQuery(sqlEntity, sql);
        //将,两边和左右括号两边的空格去除(考虑","、"("、")"左右两边会出现一个空格的情况) 最后将空格替换为指定分隔符便于后续解析字符串
        String[] items = sql.replaceAll(" +, +",",")
                .replaceAll(" +,",",")
                .replaceAll(", +",",")
                .replaceAll("\\( +","(")
                .replaceAll(" +\\)",")")
                .replaceAll(" +",SEPARATE).trim().toLowerCase().split(SEPARATE);
        if(items.length < 4){
            throw new RuntimeException("SQL resolve error \ncause by : sql is illegal");
        }
        //如果有DISTINCT关键字则从索引为2开始解析
        if(KeyWord.DISTINCT.same(items[1])){ //distinct存在
            resolve(sqlEntity,items,2);
            //设置去重标记为true
            sqlEntity.setDistinct(true);
        }else{
            resolve(sqlEntity,items,1);
        }
    }

    /**
     * 子查询情况
     * 1、select (select ...) from ...
     * 2、select ... from (select ...)
     * 3、select ... from ... where ... (select...)
     * 4、select ... from ... group by ... having ... (select...)
     * 只对select列表和form列表进行解析,剩下的分情况解析
     * 共有12种合法SQL形式
     * 1、select ... from ...
     * 2、select ... from ... where ...
     * 3、select ... from ... where ... order by ...
     * 4、select ... from ... where ... group by ...
     * 5、select ... from ... where ... group by ... having ...
     * 6、select ... from ... where ... group by ... having ... order by ...
     * 7、select ... from ... where ... group by ... order by ...
     * 8、select ... from ... order by ...
     * 9、select ... from ... group by ...
     * 10、select ... from ... group by ... having ...
     * 11、select ... from ... group by ... order by ...
     * 12、select ... from ... group by ... having ... order by ...
     */
    private void resolve(SelectSQLEntity sqlEntity,String[] items,int start){
        //查询列表解析
        selectListResolve(sqlEntity,items,start);
        if(!KeyWord.FROM.same(items[start + 1])){
            throw new RuntimeException("SQL resolve error \ncause by : sql is illegal");
        }
        //进行form子句解析
        start = fromResolve(sqlEntity, items, start);
        //开始进行where、group、order子句的解析
        if(KeyWord.WHERE.same(items[start])){
            //当前SQL为select ... from ... where ...形式
            whereResolve(sqlEntity,items,start);
        }else if(start + 1 < items.length && KeyWord.GROUP.same(items[start]) && KeyWord.BY.same(items[start + 1])){
            //当前SQL为select ... from ... group by ...形式
            groupResolve(sqlEntity,items,start);
        }else if(start + 1 < items.length && KeyWord.ORDER.same(items[start]) && KeyWord.BY.same(items[start + 1])){
            //当前SQL为select ... from ... order by ...形式
            orderResolve(sqlEntity,items,start);
        }else if(!";".equals(items[start])){ //SQL语句结构错误
            throw new RuntimeException("You have an error in your SQL syntax \ncause by : sql struct error");
        }
    }

    /**
     * 查询列表解析
     */
    private void selectListResolve(SelectSQLEntity sqlEntity, String[] items, int start) {
        String[] selectList = items[start].replaceAll(" +",SEPARATE).split(SEPARATE);
        for (String s : selectList) {
            if (s.startsWith(AbstractSQLResolver.SEPARATE_SUB_QUERY)) {
                //出现子查询语句 获取子查询索引
                setSubQueryToSQLEntityAndResolve(KeyWord.SELECT.getMeaning(),s, sqlEntity);
            }
        }
        sqlEntity.setQueryFields(Arrays.asList(selectList));
    }

    public void setSubQuerySuperEntity(SQLEntity subQuerySuperEntity) {
        this.subQuerySuperEntity = subQuerySuperEntity;
    }

    //获取子查询的父存储实体
    @Override
    public SQLEntity getSubQuerySuperEntity() {
        if(subQuerySuperEntity == null){
            return getSQLEntity(SelectSQLEntity.class);
        }
        return subQuerySuperEntity;
    }

    /**
     * 获取子查询语句设置到当前SQL存储实体中,并对此子查询进行解析
     */
    @Override
    public void setSubQueryToSQLEntityAndResolve(String storageType,String indexStr,SQLEntity parentSQLEntity){
        SelectSQLEntity sqlEntity = null;
        if(parentSQLEntity instanceof SelectSQLEntity){
           sqlEntity = (SelectSQLEntity) parentSQLEntity;
        }
        if(sqlEntity == null){
            throw new RuntimeException("SQL resolve error \ncause by : selectEntity set fail current type : " + parentSQLEntity.getClass());
        }
        int index = Integer.parseInt(String.valueOf(indexStr.charAt(1)));
        //注意此时子查询语句必须从父SQLEntity中获取,子类中还未存储(解析算法导致将所有子查询解析到父类中了)
        String subQuerySQL = getSubQuerySuperEntity().getSubQuerySQLList().get(index);
        SelectSQLEntity selectSQLEntity = new SelectSQLEntity();
        resolve(selectSQLEntity,subQuerySQL);
        if(KeyWord.SELECT.same(storageType)){
            sqlEntity.getSelectSubQueryEntities().add(selectSQLEntity);
        }else if(KeyWord.FROM.same(storageType)){
            sqlEntity.getFormSubQueryEntities().add(selectSQLEntity);
        }else if(KeyWord.WHERE.same(storageType)){
            sqlEntity.getWhereSubQueryEntities().add(selectSQLEntity);
        }else if(KeyWord.HAVING.same(storageType)){
            sqlEntity.getHavingSubQueryEntities().add(selectSQLEntity);
        }else{
            throw new RuntimeException("SQL resolve error \ncause by : sub query sql storage error : " + storageType);
        }
    }

    /**
     * order by子句解析
     * select ... from ... order by ...形式解析
     * 只有这一种情况
     */
    public void orderResolve(SQLEntity entity, String[] items, int start) {
        SelectSQLEntity sqlEntity = null;
        if(entity instanceof SelectSQLEntity){
            sqlEntity = (SelectSQLEntity) entity;
        }
        if(sqlEntity == null){
            throw new RuntimeException("SQL resolve error \ncause by : selectEntity set fail current type : " + entity.getClass());
        }
        //order by条件字符串
        StringBuilder orderByCondition = new StringBuilder();
        //跳过order by两个关键字开始解析
        for(int i = start + 2;i < items.length;i++){
            //只有以";"结尾这一种方式
            if(!";".equals(items[i])){
                orderByCondition.append(items[i]).append(SEPARATE);
            }else{
                break;
            }
        }
        sqlEntity.setOrderByCondition(orderByCondition.toString());
    }

    /**
     * group by子句解析
     * select ... from ... group by ... 形式解析
     * 包含以下四种情况：
     * 1、group by ...
     * 2、group by ... having ...
     * 3、group by ... order by ...
     * 4、group by ... having ... order by ...
     */
    public void groupResolve(SQLEntity entity, String[] items, int start) {
        SelectSQLEntity sqlEntity = null;
        if(entity instanceof SelectSQLEntity){
            sqlEntity = (SelectSQLEntity) entity;
        }
        if(sqlEntity == null){
            throw new RuntimeException("SQL resolve error \ncause by : selectEntity set fail current type : " + entity.getClass());
        }
        //group by条件字符串
        StringBuilder groupByCondition = new StringBuilder();
        //跳过group by关键字开始解析
        start += 2;
        if(start < items.length){
            //拼接group by的内容
            groupByCondition.append(items[start++]);
            sqlEntity.setGroupByCondition(groupByCondition.toString());
            if(KeyWord.HAVING.same(items[start])){//group by ... having ...形式
                //having条件字符串
                StringBuilder havingCondition = new StringBuilder();
                for(int i = start + 1;i < items.length;i++){
                    if(items[i].startsWith("#")){
                        setSubQueryToSQLEntityAndResolve(KeyWord.HAVING.getMeaning(),items[i],sqlEntity);
                    }
                    //有以";"和"order by"结尾两种方式
                    if(!KeyWord.ORDER.same(items[i]) && !";".equals(items[i])){
                        havingCondition.append(items[i]).append(SEPARATE);
                    }else{
                        start = i;
                        break;
                    }
                }
                sqlEntity.setHavingCondition(havingCondition.toString());
                if(start + 1 < items.length
                        && KeyWord.ORDER.same(items[start]) && KeyWord.BY.same(items[start + 1])){
                    //有order by 当前SQL为第4种情况
                    orderResolve(sqlEntity,items,start);
                }else if(!";".equals(items[start])){
                    //既不以";"结尾也不以order by结尾的group by ... having ...形式的SQL为非法SQL
                    throw new RuntimeException("You have an error in your SQL syntax \n cause by : near group by");
                }
            }else if(start + 1 < items.length
                    && KeyWord.ORDER.same(items[start]) && KeyWord.BY.same(items[start + 1])){
                //group by ... order by ...形式
                orderResolve(sqlEntity,items,start);
            }else if(!";".equals(items[start])){
                //不以";"、order by、having任一种方式结尾的group by ...形式的SQL为非法SQL
                throw new RuntimeException("You have an error in your SQL syntax \n cause by : near group by");
            }
        }else{
            throw new RuntimeException("You have an error in your SQL syntax \n cause by : near group by");
        }
    }

    @Override
    public String getName() {
        return SQLLanguageType.SELECT.getTypeName();
    }
}