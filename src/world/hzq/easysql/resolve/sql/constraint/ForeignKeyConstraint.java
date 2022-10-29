package world.hzq.easysql.resolve.sql.constraint;

import world.hzq.easysql.resolve.sql.common.AbstractSQLResolver;
import world.hzq.easysql.resolve.sql.common.KeyWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForeignKeyConstraint extends AbstractConstraint{
    //引用字段列表
    private final List<String> referenceColumnList = new ArrayList<>();
    //引用表名
    private String referenceTableName;

    /**
     * foreign key(?,?) references tableName(?,?)
     */
    @Override
    protected void foreignKeyResolve(String constraintStr) {
        String[] items = constraintStr.split(AbstractSQLResolver.SEPARATE);
        if(items.length < 10){
            throw new RuntimeException("create table error \n cause by : foreign key constraint illegal");
        }
        if(ConstraintType.FOREIGN_KEY.same(items[0] + AbstractSQLResolver.SEPARATE + items[1])){
            //拼接所有的列名并解析到集合中
            StringBuilder constraintColumnStr = new StringBuilder();
            StringBuilder referenceColumnStr = new StringBuilder();
            //解析约束列
            int start = 3;
            if(!"(".equals(items[2])){
                throw new RuntimeException("create table error \n cause by : foreign key constraint illegal check '('");
            }
            for (int i = start; i < items.length; i++) {
                if(!"(".equals(items[i]) && !")".equals(items[i])){
                    constraintColumnStr.append(items[i]);
                }else{
                    start = i + 1;
                    break;
                }
            }
            if(!")".equals(items[start - 1])){
                throw new RuntimeException("create table error \n cause by : foreign key constraint illegal check ')'");
            }
            if(!KeyWord.REFERENCES.same(items[start])){
                throw new RuntimeException("create table error \n cause by : foreign key constraint illegal near " + items[start]);
            }
            //括号不合法
            if(!"(".equals(items[start + 2]) || !")".equals(items[items.length - 1])){
                throw new RuntimeException("create table error \n cause by : foreign key constraint illegal check '(' or ')' ");
            }
            //设置引用表名
            referenceTableName = items[start + 1];
            //设置约束字段列表
            constraintColumnList.addAll(Arrays.asList(constraintColumnStr.toString().split(",")));
            //解析引用列
            for (int i = start + 3; i < items.length; i++) {
                if(!")".equals(items[i])){
                    referenceColumnStr.append(items[i]);
                }else {
                    break;
                }
            }
            //设置引用字段列表
            referenceColumnList.addAll(Arrays.asList(referenceColumnStr.toString().split(",")));
        }else{
            throw new RuntimeException("create table error \n cause by : foreign key constraint illegal near " + items[0]);
        }
    }

    public List<String> getReferenceColumnList() {
        return referenceColumnList;
    }

    public String getReferenceTableName() {
        return referenceTableName;
    }

    @Override
    public String toString() {
        return "ForeignKeyConstraint{" +
                "referenceColumnList=" + referenceColumnList +
                ", referenceTableName='" + referenceTableName + '\'' +
                ", constraintName='" + constraintName + '\'' +
                ", constraintType=" + constraintType +
                ", constraintContent='" + constraintContent + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", constraintColumnList=" + constraintColumnList +
                '}';
    }
}
