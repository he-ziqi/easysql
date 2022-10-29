package world.hzq.easysql.resolve.sql.constraint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象约束
 */
public abstract class AbstractConstraint implements ConstraintResolve, Serializable {
    //约束名称
    public String constraintName;
    //约束类型
    public ConstraintType constraintType;
    //约束内容
    public String constraintContent;
    //约束数据库名
    public String databaseName;
    //约束表名
    public String tableName;
    //约束字段列表
    public final List<String> constraintColumnList = new ArrayList<>();


    protected void checkResolve(String constraintStr){}

    protected void foreignKeyResolve(String constraintStr){}

    protected void uniqueResolve(String constraintStr){}

    protected void primaryKeyResolve(String constraintStr){}

    /**
     * 只解析单独为一列的约束 追加在列定义后的约束在列解析时会直接设置到对应的约束类中
     */
    @Override
    public void resolve(String constraintStr) {
        if(constraintStr == null){
            throw new RuntimeException("create table error \n cause by : illegal constraint");
        }
        setConstraintContent(constraintStr);
        if(constraintStr.contains(ConstraintType.PRIMARY_KEY.getMeaning().toUpperCase()) ||
                constraintStr.contains(ConstraintType.PRIMARY_KEY.getMeaning().toLowerCase())){
            //主键约束
            setConstraintType(ConstraintType.PRIMARY_KEY);
            primaryKeyResolve(constraintStr);
        }else if(constraintStr.contains(ConstraintType.UNIQUE.getMeaning().toUpperCase()) ||
                constraintStr.contains(ConstraintType.UNIQUE.getMeaning().toLowerCase())){
            //唯一性约束
            setConstraintType(ConstraintType.UNIQUE);
            uniqueResolve(constraintStr);
        }else if(constraintStr.contains(ConstraintType.FOREIGN_KEY.getMeaning().toUpperCase()) ||
                constraintStr.contains(ConstraintType.FOREIGN_KEY.getMeaning().toLowerCase())){
            //外键约束
            setConstraintType(ConstraintType.FOREIGN_KEY);
            foreignKeyResolve(constraintStr);
        }else if(constraintStr.contains(ConstraintType.CHECK.getMeaning().toUpperCase()) ||
                constraintStr.contains(ConstraintType.CHECK.getMeaning().toLowerCase())){
            //自定义约束
            setConstraintType(ConstraintType.CHECK);
            checkResolve(constraintStr);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getConstraint(Class<T> constraintClass) {
        return (T) this;
    }

    public List<String> getConstraintColumnList() {
        return constraintColumnList;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(ConstraintType constraintType) {
        this.constraintType = constraintType;
    }

    public String getConstraintContent() {
        return constraintContent;
    }

    public void setConstraintContent(String constraintContent) {
        this.constraintContent = constraintContent;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "AbstractConstraint{" +
                "constraintName='" + constraintName + '\'' +
                ", constraintType=" + constraintType +
                ", constraintContent='" + constraintContent + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", constraintColumnList=" + constraintColumnList +
                '}';
    }
}
