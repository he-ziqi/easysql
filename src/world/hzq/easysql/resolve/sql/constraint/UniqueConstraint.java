package world.hzq.easysql.resolve.sql.constraint;

import world.hzq.easysql.resolve.sql.common.AbstractSQLResolver;

import java.util.Arrays;

/**
 * 唯一性约束
 */
public class UniqueConstraint extends AbstractConstraint{
    public UniqueConstraint(String column){
        constraintColumnList.add(column);
        setConstraintType(ConstraintType.UNIQUE);
    }
    public UniqueConstraint(){}
    /**
     * unique(?,?)
     */
    @Override
    protected void uniqueResolve(String constraintStr) {
        String[] items = constraintStr.split(AbstractSQLResolver.SEPARATE);
        if(items.length < 4){
            throw new RuntimeException("create table error \n cause by : unique constraint illegal");
        }
        if(ConstraintType.UNIQUE.same(items[0])){
            //拼接所有的列名并解析到集合中
            StringBuilder columnStr = new StringBuilder();
            //括号不合法
            if(!"(".equals(items[1]) || !")".equals(items[items.length - 1])){
                throw new RuntimeException("create table error \n cause by : unique constraint illegal check '(' or ')' ");
            }
            for (int i = 2; i < items.length; i++) {
                if(!")".equals(items[i])){
                    columnStr.append(items[i]);
                }else{
                    break;
                }
            }
            String[] columnList = columnStr.toString().split(",");
            constraintColumnList.addAll(Arrays.asList(columnList));
        }else{
            throw new RuntimeException("create table error \n cause by : unique constraint illegal near " + items[0]);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
