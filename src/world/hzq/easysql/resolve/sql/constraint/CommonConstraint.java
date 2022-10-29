package world.hzq.easysql.resolve.sql.constraint;

/**
 * 包含只能在列定义后面添加的约束 且无内容
 * auto_increment
 * zerofill
 * not null
 */
public class CommonConstraint extends AbstractConstraint{

    public CommonConstraint(ConstraintType constraintType,String column){
        this.setConstraintType(constraintType);
        constraintColumnList.add(column);
    }

    @Override
    public ConstraintType getConstraintType() {
        return super.getConstraintType();
    }

}
