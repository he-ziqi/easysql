package world.hzq.easysql.config;

import world.hzq.easysql.storage.util.IOUtil;

import java.util.Properties;

public class Config {
    //配置文件存放位置
    private static final String CONFIG_FILE_LOCATION = "src/world/hzq/easysql/config/config.properties";
    //默认根目录存放位置
    private static final String DEFAULT_STORAGE_LOCATION = "src/world/hzq/easysql/easysql";
    //数据存放位置的根目录
    private static final String STORAGE_LOCATION;
    //系统数据库目录存放位置
    private static final String SYS_DATABASE_DIRECTORY;
    //文件分隔符
    public static final String FILE_SEPARATOR = "/";
    //表结构文件格式后缀
    public static final String TABLE_STRUCT_SUFFIX = ".frm";
    //表数据文件格式后缀
    public static final String TABLE_DATA_SUFFIX = ".ibd";
    static {
        Properties config = IOUtil.getConfig();
        String storageBase = config.getProperty("storageBase");
        if(storageBase == null || "".equals(storageBase)){
            STORAGE_LOCATION = DEFAULT_STORAGE_LOCATION;
        }else{
            STORAGE_LOCATION = storageBase;
        }
        SYS_DATABASE_DIRECTORY = STORAGE_LOCATION + FILE_SEPARATOR + "information_schema";
    }
    public static String getStorageLocation(){
        return STORAGE_LOCATION;
    }
    public static String getConfigFileLocation() {
        return CONFIG_FILE_LOCATION;
    }
    public static String getSysDatabaseDirectory(){return SYS_DATABASE_DIRECTORY;}

}
