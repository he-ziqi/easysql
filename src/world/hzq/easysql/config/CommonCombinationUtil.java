package world.hzq.easysql.config;

/**
 * 组合表名
 */
public class CommonCombinationUtil {
    private CommonCombinationUtil(){}

    /**
     * 获取数据库所在的文件目录
     */
    public static String getTableDir(String database){
        return Config.getStorageLocation() + Config.FILE_SEPARATOR + database;
    }

    /**
     * 获取表文件所在的路径
     * @param struct 是否为表结构文件
     */
    public static String getTable(String database,String tableName,boolean struct){
        String res = getTableDir(database) + Config.FILE_SEPARATOR + tableName;
        if(struct){
            res += Config.TABLE_STRUCT_SUFFIX;
        }else{
            res += Config.TABLE_DATA_SUFFIX;
        }
        return res;
    }
}
