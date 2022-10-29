package world.hzq.easysql.storage;

import world.hzq.easysql.storage.struct.TableStruct;

import java.util.LinkedList;
import java.util.List;

/**
 * 表空间
 * ibd数据文件+frm表结构文件
 */
public class TableSpace{

    //leaf node extent 数据段 B+树的叶子节点
    //non-leaf node extent索引段 B+树的非叶子节点
    public List<Extent> extentList = new LinkedList<>();

    public List<Extent> getExtentList() {
        return extentList;
    }

    public void setExtentList(List<Extent> extentList) {
        this.extentList = extentList;
    }

    //只序列化数据文件,表结构单独序列化
    private transient TableStruct tableStruct;

    public TableStruct getTableStruct() {
        return tableStruct;
    }

    public void setTableStruct(TableStruct tableStruct) {
        this.tableStruct = tableStruct;
    }
}
