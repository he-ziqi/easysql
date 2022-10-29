package world.hzq.easysql.storage.util;

import world.hzq.easysql.config.CommonCombinationUtil;
import world.hzq.easysql.config.Config;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 */
public class FileUtil {
    private FileUtil(){}

    /**
     * 文件是否存在
     */
    public static boolean isExists(File file){
        return file.exists();
    }

    /**
     * 创建目录
     */
    public static void createDirectory(String directory){
        File file = new File(directory);
        if(file.exists() && file.isDirectory()){
            return;
        }
        boolean mkdirs = file.mkdirs();
        if(!mkdirs){
            throw new RuntimeException("create directory fail");
        }
    }

    /**
     * 创建表文件
     */
    public static void createTableFile(String database,String tableName){
        File structFileDirectory = new File(CommonCombinationUtil.getTableDir(database));
        File dataFileDirectory = new File(CommonCombinationUtil.getTableDir(database));
        //表格式文件
        File structFile = new File(structFileDirectory,tableName + Config.TABLE_STRUCT_SUFFIX);
        //表数据文件
        File dataFile = new File(dataFileDirectory,tableName + Config.TABLE_DATA_SUFFIX);
        if(!structFileDirectory.exists()){ //目录不存在
            boolean mkdir = structFileDirectory.mkdirs();
            if(!mkdir){
                throw new RuntimeException("create table struct file fail");
            }
            createFile(structFile);
        }else{ //目录存在
            if(!structFile.exists()){ //结构文件不存在
                createFile(structFile);
            }
        }
        if(!dataFileDirectory.exists()){
            boolean mkdirs = dataFileDirectory.mkdirs();
            if(!mkdirs){
                throw new RuntimeException("create table data file fail");
            }
            createFile(dataFile);
        }else {
            if(!dataFile.exists()){ //数据文件不存在
                createFile(dataFile);
            }
        }
    }

    //创建文件
    private static void createFile(File file){
        try {
            boolean newFile = file.createNewFile();
            if(!newFile){
                throw new RuntimeException("create data file fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
