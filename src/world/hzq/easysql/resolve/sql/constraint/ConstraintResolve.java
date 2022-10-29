package world.hzq.easysql.resolve.sql.constraint;

/**
 * 约束解析
 */
public interface ConstraintResolve {
    void resolve(String constraintStr);
    <T> T getConstraint(Class<T> constraintClass);
}
