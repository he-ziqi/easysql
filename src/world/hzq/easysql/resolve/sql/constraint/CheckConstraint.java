package world.hzq.easysql.resolve.sql.constraint;

import world.hzq.easysql.resolve.sql.common.AbstractSQLResolver;

public class CheckConstraint extends AbstractConstraint{
    //约束字符串
    private String checkContent;

    public CheckConstraint(){}
    public CheckConstraint(String checkContent){
        this.checkContent = checkContent;
        setConstraintType(ConstraintType.CHECK);
    }

    /**
     * check (expression)
     */
    @Override
    protected void checkResolve(String constraintStr) {
        String[] items = constraintStr.split(AbstractSQLResolver.SEPARATE);
        if(items.length < 4){
            throw new RuntimeException("create table error \n cause by : check constraint illegal");
        }
        if(!"(".equals(items[1]) || !")".equals(items[items.length - 1])){
            throw new RuntimeException("create table error \n cause by : check constraint illegal check '(' or ')' ");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < items.length; i++) {
            if(!")".equals(items[i])){
                sb.append(items[i]);
            }else {
                break;
            }
        }
        checkContent = sb.toString();
        //约束字段
        constraintColumnList.add(items[0]);
        setConstraintContent("check(" + checkContent + ")");
    }

    public String getCheckContent() {
        return checkContent;
    }

    @Override
    public String toString() {
        return "CheckConstraint{" +
                "checkContent='" + checkContent + '\'' +
                ", constraintName='" + constraintName + '\'' +
                ", constraintType=" + constraintType +
                ", constraintContent='" + constraintContent + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", constraintColumnList=" + constraintColumnList +
                '}';
    }
}
