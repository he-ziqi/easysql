package world.hzq.easysql.resolve.sql.constraint;

/**
 * 约束类型
 */
public enum ConstraintType {
    PRIMARY_KEY("primary~key"),
    AUTO_INCREMENT("auto_increment"),
    NOT_NULL("not~null"),
    UNIQUE("unique"),
    DEFAULT("default"),
    ZEROFILL("zerofill"),
    FOREIGN_KEY("foreign~key"),
    CHECK("check");

    private final String name;
    ConstraintType(final String name){
        this.name = name;
    }

    public boolean same(String name){
        if(this.name.toUpperCase().equals(name) || this.name.toLowerCase().equals(name)){
            return true;
        }
        return false;
    }

    public String getMeaning() {
        return name;
    }
}
