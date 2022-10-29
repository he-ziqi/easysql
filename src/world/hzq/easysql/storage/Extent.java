package world.hzq.easysql.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * 区,存放64个连续空间的数据页
 * 大小：1MB
 */
public class Extent {
    public static int DEFAULT_SIZE = 64;
    //区号
    public int extentId;
    //数据页列表
    public List<Page> pageList = new ArrayList<>(DEFAULT_SIZE);

    public List<Page> getPageList() {
        return pageList;
    }
}
