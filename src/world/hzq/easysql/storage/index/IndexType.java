package world.hzq.easysql.storage.index;

public enum IndexType{
    //非索引类型
    NOT_INDEX("notIndex"),
    //聚簇索引
    CLUSTERED("clustered"),
    //非聚簇索引
    NON_CLUSTERED("nonClustered");
    private final String name;
    IndexType(final String name){
        this.name = name;
    }
    public boolean same(String name){
        return name != null && this.name.toUpperCase().equals(name.trim().toUpperCase());
    }
}