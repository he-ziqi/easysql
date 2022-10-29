package world.hzq.easysql.resolve.sql.constraint;

import world.hzq.easysql.resolve.sql.common.AbstractSQLResolver;

import java.util.Arrays;

/**
 * 主键约束
 */
public class PrimaryKeyConstraint extends AbstractConstraint{
    public PrimaryKeyConstraint(String column){
        constraintColumnList.add(column);
        setConstraintType(ConstraintType.PRIMARY_KEY);
    }
    public PrimaryKeyConstraint(){}
    /**
     * primary key(?,?,?)
     */
    @Override
    protected void primaryKeyResolve(String constraintStr) {
        String[] items = constraintStr.split(AbstractSQLResolver.SEPARATE);
        if(items.length < 5){
            throw new RuntimeException("create table error \n cause by : primary key constraint illegal");
        }
        if(ConstraintType.PRIMARY_KEY.same(items[0] + items[1])){
            //括号不合法
            if(!"(".equals(items[2]) || !")".equals(items[items.length - 1])){
                throw new RuntimeException("create table error \n cause by : primary key constraint illegal check '(' or ')' ");
            }
            //拼接所有的列名并解析到集合中
            StringBuilder columnStr = new StringBuilder();
            for (int i = 3; i < items.length; i++) {
                if(!")".equals(items[i])){
                    columnStr.append(items[i]);
                }else{
                    break;
                }
            }
            String[] columnList = columnStr.toString().split(",");
            constraintColumnList.addAll(Arrays.asList(columnList));
        }else{
            throw new RuntimeException("create table error \n cause by : primary key constraint illegal near " + items[0]);
        }
    }

}
