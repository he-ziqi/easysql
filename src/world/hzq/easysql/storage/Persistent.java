package world.hzq.easysql.storage;

import world.hzq.easysql.resolve.sql.common.SQLEntity;

/**
 * 持久化接口
 * ibd：表数据文件
 * frm：表结构文件
 */
public interface Persistent {
    //将SQLEntity持久化到硬盘中
    void storage(SQLEntity sqlEntity);
}