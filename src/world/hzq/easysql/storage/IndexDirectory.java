package world.hzq.easysql.storage;

import java.io.Serializable;

public class IndexDirectory implements Serializable {
    //最小索引与最大索引
    public String minIndex;
    public String maxIndex;

    public IndexDirectory(String minIndex, String maxIndex) {
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
    }

}
