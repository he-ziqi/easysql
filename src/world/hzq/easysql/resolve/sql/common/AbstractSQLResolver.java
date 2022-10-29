package world.hzq.easysql.resolve.sql.common;

import world.hzq.easysql.resolve.sql.entity.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 抽象SQL解析器
 */
public abstract class AbstractSQLResolver implements SQLResolver,SQLInfo {
    private String sql;
    private final List<String> sqlList = new LinkedList<>();
    private SQLEntity sqlEntity;
    public static String currentDatabase;
    //解析时的分隔符
    public static final String SEPARATE = "~";
    //子查询解析时占位符
    public static final String SEPARATE_SUB_QUERY = "#";
    //列结束分隔符
    public static final String SEPARATE_COLUMN = "@";

    //order子句解析
    public abstract void orderResolve(SQLEntity sqlEntity, String[] items, int start);

    //group子句解析
    public abstract void groupResolve(SQLEntity sqlEntity, String[] items, int start);

    //解析子查询并存储
    public abstract void setSubQueryToSQLEntityAndResolve(String storageType,String indexStr,SQLEntity parentSQLEntity);

    //select语句解析
    public abstract void selectResolve(String sql);

    //delete语句解析
    public abstract void deleteResolve(String sql);

    //insert语句解析
    public abstract void insertResolve(String sql);

    //update语句解析
    public abstract void updateResolve(String sql);

    //create语句解析
    public abstract void createResolve(String sql);

    //alter语句解析
    public abstract void alterResolve(String sql);

    //drop语句解析
    public abstract void dropResolve(String sql);

    //truncate语句解析
    public abstract void truncateResolve(String sql);

    //comment语句解析
    public abstract void commentResolve(String sql);

    //grant语句解析
    public abstract void grantResolve(String sql);

    //revoke语句解析
    public abstract void revokeResolve(String sql);

    //commit语句解析
    public abstract void commitResolve(String sql);

    //rollback语句解析
    public abstract void rollbackResolve(String sql);

    //savePoint语句解析
    public abstract void savePointResolve(String sql);

    //命令解析
    public abstract void orderResolve(String sql);

    /**
     * SQL解析方法
     */
    @Override
    public final void resolve(String sql) {
        this.sql = sql;
        boolean illegal = isIllegal(sql);
        if(illegal){
            throw new RuntimeException("You have an error in your SQL syntax");
        }
        //给末尾;前添加空格便于后续的解析操作
        sql = sql.trim().substring(0,sql.trim().length() - 1) + " ;";
        String[] items = sql.trim().split("\\s+");
        SQLEntity sqlEntity;
        if(KeyWord.SELECT.same(items[0])){
            sqlEntity = new SelectSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.SELECT);
            selectResolve(sql);
        }else if(KeyWord.DELETE.same(items[0])){
            sqlEntity = new DeleteSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.DELETE);
            deleteResolve(sql);
        }else if(KeyWord.INSERT.same(items[0])){
            sqlEntity = new InsertSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.INSERT);
            insertResolve(sql);
        }else if(KeyWord.UPDATE.same(items[0])){
            sqlEntity = new UpdateSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.UPDATE);
            updateResolve(sql);
        }else if(KeyWord.CREATE.same(items[0])){
            sqlEntity = new CreateSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.CREATE);
            createResolve(sql);
        }else if(KeyWord.ALTER.same(items[0])){
            sqlEntity = new AlterSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.ALTER);
            alterResolve(sql);
        }else if(KeyWord.DROP.same(items[0])){
            sqlEntity = new DropSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.DROP);
            dropResolve(sql);
        }else if(KeyWord.TRUNCATE.same(items[0])){
            sqlEntity = new TruncateSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.TRUNCATE);
            truncateResolve(sql);
        }else if(KeyWord.COMMENT.same(items[0])){
            sqlEntity = new CommentSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.COMMENT);
            commentResolve(sql);
        }else if(KeyWord.GRANT.same(items[0])){
            sqlEntity = new GrantSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.GRANT);
            grantResolve(sql);
        }else if(KeyWord.REVOKE.same(items[0])){
            sqlEntity = new RevokeSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.REVOKE);
            revokeResolve(sql);
        }else if(KeyWord.COMMIT.same(items[0])){
            sqlEntity = new CommitSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.COMMIT);
            commitResolve(sql);
        }else if(KeyWord.ROLLBACK.same(items[0])){
            sqlEntity = new RollbackSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.ROLLBACK);
            rollbackResolve(sql);
        }else if(KeyWord.SAVEPOINT.same(items[0])){
            sqlEntity = new SavePointSQLEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.SAVEPOINT);
            savePointResolve(sql);
        }else if(KeyWord.SHOW.same(items[0]) || KeyWord.USE.same(items[0]) || KeyWord.DESC.same(items[0])){
            sqlEntity = new OrderEntity();
            setSQLEntity(sqlEntity);
            sqlEntity.setSqlLanguageType(SQLLanguageType.ORDER);
            orderResolve(sql);
        }else{
            //非法SQL语句
            throw new RuntimeException("sql resolve error\ncause by : sql is illegal");
        }
        //保存正常解析的SQL语句
        getSqlList().add(sql);
    }

    /**
     * from子句解析 解析查询表名和表连接关系
     * @return 返回解析完成后的索引位置
     */
    protected int fromResolve(SQLEntity sqlEntity,String[] items,int start){
        List<String> tables = new ArrayList<>();
        List<String> relation = new ArrayList<>();
        //from子句解析
        for(int i = start + 2;i < items.length;){
            if(KeyWord.WHERE.same(items[i]) || ";".equals(items[i])){
                start = i;
                break;
            }
            //表名及其别名拼接
            StringBuilder tableNameAndAlias = new StringBuilder();
            //未到达尾部并且当前字符串不是inner、left、right、where、group、having、order时拼接表名
            while (i + 1 < items.length && !KeyWord.INNER.same(items[i]) && !KeyWord.LEFT.same(items[i])
                    && !KeyWord.RIGHT.same(items[i]) && !KeyWord.JOIN.same(items[i + 1]) && !KeyWord.WHERE.same(items[i])
                    && !KeyWord.GROUP.same(items[i]) && !KeyWord.HAVING.same(items[i]) && !KeyWord.ORDER.same(items[i])){
                //form子句中出现子查询
                if(items[i].startsWith(SEPARATE_SUB_QUERY)){
                    setSubQueryToSQLEntityAndResolve(KeyWord.FROM.getMeaning(),items[i],sqlEntity);
                }
                tableNameAndAlias.append(items[i]).append(SEPARATE);
                i++;
            }
            //如果是空串则不需要添加
            boolean isEmpty = !"".equals(tableNameAndAlias.toString().trim());
            if(isEmpty){
                tables.add(tableNameAndAlias.toString());
            }
            //清空上一个表的拼接结果
            tableNameAndAlias.delete(0,tableNameAndAlias.length());
            //如果表名拼接完后出现了其它关键字或";"则停止表名的拼接
            if(KeyWord.WHERE.same(items[i]) || KeyWord.GROUP.same(items[i]) || KeyWord.HAVING.same(items[i])
                    || KeyWord.ORDER.same(items[i]) || ";".equals(items[i])){
                start = i;
                break;
            }
            //后两个字符串不是表连接的关键字
            if(!(i + 1 < items.length && KeyWord.JOIN.same(items[i + 1]) &&
                    (KeyWord.INNER.same(items[i]) || KeyWord.LEFT.same(items[i]) || KeyWord.RIGHT.same(items[i])))){
                //并且后续的第三个字符串不是where、group、having、order关键字 则SQL语法错误
                if(!(i + 2 < items.length &&
                        (KeyWord.WHERE.same(items[i + 2]) || KeyWord.GROUP.same(items[i + 2])
                                || KeyWord.HAVING.same(items[i + 2]) || KeyWord.ORDER.same(items[i + 2])))){
                    throw new RuntimeException("You have an error in your SQL syntax \ncause by : near join");
                }
                start = i;
                break;
            }
            i += 2;
            //表名字符串拼接
            while(i < items.length && !KeyWord.ON.same(items[i]) && !KeyWord.WHERE.same(items[i]) && !KeyWord.GROUP.same(items[i])
                    && !KeyWord.HAVING.same(items[i]) && !KeyWord.ORDER.same(items[i]) && !";".equals(items[i])){
                //form子句中出现子查询
                if(items[i].startsWith(SEPARATE_SUB_QUERY)){
                    setSubQueryToSQLEntityAndResolve(KeyWord.FROM.getMeaning(),items[i],sqlEntity);
                }
                tableNameAndAlias.append(items[i]).append(SEPARATE);
                i++;
            }

            isEmpty = !"".equals(tableNameAndAlias.toString().trim());
            //空串不需要添加
            if(isEmpty){
                tables.add(tableNameAndAlias.toString());
            }
            //表无关系连接时(没有on语句)需要跳出循环
            if(!KeyWord.ON.same(items[i])){
                start = i;
                break;
            }
            i++;
            StringBuilder tableRelation = new StringBuilder();
            //表关系字符串拼接
            while(i < items.length && !KeyWord.LEFT.same(items[i]) && !KeyWord.RIGHT.same(items[i]) &&
                    !KeyWord.INNER.same(items[i]) && !KeyWord.WHERE.same(items[i]) &&
                    !KeyWord.GROUP.same(items[i]) && !KeyWord.ORDER.same(items[i]) && !";".equals(items[i])){
                tableRelation.append(items[i]).append(SEPARATE);
                i++;
            }
            //空串不需要添加
            if(!"".equals(tableRelation.toString().trim())){
                relation.add(tableRelation.toString());
            }
            //出现where group order关键字时from子句解析结束
            if(i < items.length &&
                    (KeyWord.WHERE.same(items[i]) || KeyWord.GROUP.same(items[i]) || KeyWord.ORDER.same(items[i]))){
                start = i;
                break;
            }
        }
        //至此select列表和from列表解析完成
        sqlEntity.setRelation(relation);
        sqlEntity.setTables(tables);
        return start;
    }

    /**
     * where子句解析
     * select ... from ... where ...形式SQL解析
     * 包含以下三种情况：
     * 1、where ...
     * 2、where ... order by ...
     * 3、where ... group by ...
     */
    protected void whereResolve(SQLEntity sqlEntity, String[] items, int start) {
        //where条件字符串
        StringBuilder whereCondition = new StringBuilder();
        //跳过where关键字开始解析
        for(int i = start + 1;i < items.length;i++){
            //";"、order、group三种结尾方式
            if(!";".equals(items[i]) &&
                    !KeyWord.GROUP.same(items[i]) && !KeyWord.ORDER.same(items[i])){
                //form子句中出现子查询
                if(items[i].startsWith(SEPARATE_SUB_QUERY)){
                    setSubQueryToSQLEntityAndResolve(KeyWord.WHERE.getMeaning(),items[i],sqlEntity);
                }
                whereCondition.append(items[i]).append(SEPARATE);
            }else{
                start = i;
                break;
            }
        }
        sqlEntity.setWhereCondition(whereCondition.toString());
        //为where ... group by...形式
        if(start + 1 < items.length &&
                KeyWord.GROUP.same(items[start]) && KeyWord.BY.same(items[start + 1])){
            groupResolve(sqlEntity,items,start);
        }else if(start + 1 < items.length &&
                KeyWord.ORDER.same(items[start]) && KeyWord.BY.same(items[start + 1])){
            //为where ... order by ...形式
            orderResolve(sqlEntity, items, start);
        }else if(!";".equals(items[start])){
            //不以group by、order by、";"结尾 为非法SQL
            throw new RuntimeException("You have an error in your SQL syntax \n cause by : near where");
        }
    }

    /**
     * 解析子查询语句,并且将子查询存储SQL实体中
     */
    public String replaceSubQuery(SQLEntity sqlEntity,String sql){
        StringBuilder sb = new StringBuilder(sql);
        int index = 0;
        while(true){
            int start = -1;
            int end = 0;
            int len = sb.length();
            int i;
            for(i = 0;i < len;i++){
                if(sb.charAt(i) == '('){
                    start = i;
                }
                if(sb.charAt(i) == ')'){
                    end = i;
                    String s = sb.substring(start, end + 1);
                    if(s.contains("select") || s.contains("SELECT")){
                        break;
                    }
                }
            }
            //无提前跳出循环则证明所有子查询已完全替换
            if(i == len){
                break;
            }
            if(start > 0 && start < end){
                //注意截取出的子查询语句需要去掉左右括号 并且要在语句末尾加上空格分号(解析规则)
                String subQuerySQL = sb.substring(start, end + 1)
                        .replaceAll("(( )*\\(( )*)", "")
                        .replaceAll("(( )*\\)( )*)", "") + " ;";
                sb.replace(start,end + 1,SEPARATE_SUB_QUERY + index++);
                sqlEntity.getSubQuerySQLList().add(subQuerySQL);
            }
        }
        return sb.toString();
    }

    /**
     * 检查SQL是否非法
     */
    @Override
    public boolean isIllegal(String sql) {
        if(sql == null || "".equals(sql)){
            return true;
        }
        char[] chs = sql.toCharArray();
        int right = chs.length - 1;
        while(right > 0 && chs[right] == ' '){
            right--;
        }
        return right > 0 && chs[right] != ';';
    }

    //获取子查询的父存储实体
    public SQLEntity getSubQuerySuperEntity(){
        return sqlEntity;
    }

    //获取当前解析的SQL语句
    public String getSql(){
        return sql;
    }

    //获取解析过的SQL语句列表
    @Override
    public List<String> getSqlList() {
        return sqlList;
    }

    //设置SQL存储实体
    @Override
    public void setSQLEntity(SQLEntity sqlEntity) {
        this.sqlEntity = sqlEntity;
    }

    //获取SQL存储实体
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getSQLEntity(Class<T> entityClass) {
        return (T) sqlEntity;
    }

    @Override
    public SQLEntity getSQLEntity() {
        return sqlEntity;
    }

    public static String getCurrentDatabase() {
        return currentDatabase;
    }

    public static void setCurrentDatabase(String currentDatabase) {
        AbstractSQLResolver.currentDatabase = currentDatabase;
    }
}