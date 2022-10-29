package world.hzq.easysql.executor;

import world.hzq.easysql.config.CommonCombinationUtil;
import world.hzq.easysql.config.Config;
import world.hzq.easysql.resolve.sql.common.KeyWord;
import world.hzq.easysql.resolve.sql.common.SQLEntity;
import world.hzq.easysql.resolve.sql.entity.CreateSQLEntity;
import world.hzq.easysql.storage.struct.TableStruct;
import world.hzq.easysql.storage.util.FileUtil;
import world.hzq.easysql.storage.util.IOUtil;

import java.io.File;

public class CreateExecutor extends ExecutorAdapter{
    @Override
    protected void executeCreate(SQLEntity sqlEntity) {
        CreateSQLEntity createSQLEntity = (CreateSQLEntity) sqlEntity;
        //获取当前create的类型
        String type = createSQLEntity.getType();
        if(KeyWord.TABLE.same(type)){ //创建表
            String database = createSQLEntity.getDatabase();
            String table = createSQLEntity.getTableStruct().getTableName();
            //创建表结构文件和表数据文件
            FileUtil.createTableFile(database,table);
            //写入表结构文件
            TableStruct struct = createSQLEntity.getTableStruct();
            String tablePath = CommonCombinationUtil.getTable(database, table, true);
            File structFile = new File(tablePath);
            //创建表的索引目录文件
            FileUtil.createTableFile(Config.getSysDatabaseDirectory(),table);
            //写入表结构文件
            IOUtil.writeObject(struct,structFile);
        }else if(KeyWord.DATABASE.same(type)){
            String database = createSQLEntity.getDatabase();
            String databaseDirectory = CommonCombinationUtil.getTableDir(database);
            //创建数据库
            FileUtil.createDirectory(databaseDirectory);
        }else if(KeyWord.INDEX.same(type)){
            //暂未实现
        }else if(KeyWord.USER.same(type)){
            //暂未实现
        }else if(KeyWord.VIEW.same(type)){
            //暂未实现
        }
    }
}
