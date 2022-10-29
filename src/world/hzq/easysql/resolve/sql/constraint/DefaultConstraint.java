package world.hzq.easysql.resolve.sql.constraint;

/**
 * 默认约束
 */
public class DefaultConstraint extends AbstractConstraint{
    private final String defaultContent;
    public DefaultConstraint(String defaultContent,String column){
        this.defaultContent = defaultContent;
        constraintColumnList.add(column);
        setConstraintType(ConstraintType.DEFAULT);
    }

    public String getDefaultContent() {
        return defaultContent;
    }

    @Override
    public String toString() {
        return "DefaultConstraint{" +
                "defaultContent='" + defaultContent + '\'' +
                ", constraintName='" + constraintName + '\'' +
                ", constraintType=" + constraintType +
                ", constraintContent='" + constraintContent + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", constraintColumnList=" + constraintColumnList +
                '}';
    }
}
