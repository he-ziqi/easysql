package world.hzq.easysql.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据页
 * 大小：16KB
 */
public class Page implements Serializable {
    //页的字节大小
    public static final int PAGE_SIZE = 16 * 1024;
    //页面中的最小记录
    public Compact infimum;
    //页面中的最大记录
    public Compact supremum;
    //存储页通用信息
    public FileHeader fileHeader = new FileHeader();
    //存储数据页的专有信息
    public PageHeader pageHeader = new PageHeader();
    //用户记录
    //public UserRecords userRecords = new UserRecords();
    //页目录(按槽位分组记录)
    public PageDirectory pageDirectory = new PageDirectory();
    //文件尾部(校验和与LSN)
    public FileTrailer fileTrailer = new FileTrailer();

    //存储页的通用信息
    public static class FileHeader implements Serializable{
        //页的校验和
        public int FIL_PAGE_SPACE_OR_CHKSUM;
        //页号
        public int FIL_PAGE_OFFSET;
        //上一个页的页号
        public int FIL_PAGE_PREV;
        //下一个页的页号
        public int FIL_PAGE_NEXT;
        //页面被最后修改时对应的LSN值
        public long FIL_PAGE_LSN;
        //该页的类型
        public short FIL_PAGE_TYPE;
        //仅在系统表空间的第一个页中定义,代表文件至少被刷新了LSN值次
        public long FIL_PAGE_FILE_FLUSH_LSN;
        //该页属于哪个表空间
        public int FIL_PAGE_ARCH_LOG_NO_OR_SPACE_ID;
    }

    //存储数据页的专有信息
    public static class PageHeader implements Serializable{
        //page Directory 中槽位的数量
        public short PAGE_N_DIR_SLOTS;
        //未使用空间最小地址,该地址之后
        public short PAGE_HEAP_TOP;
        //本页的堆中记录的数量（包含最小记录和最大记录）
        public short PAGE_N_HEAP;
        //表示已标记为删除的记录组成的垃圾单链表头结点在页面中的偏移量
        public short PAGE_FREE;
        //已删除记录占用的字节数
        public short PAGE_GARBAGE;
        //最后插入记录的位置
        public short PAGE_LAST_INSERT;
        //记录插入的方向
        public short PAGE_DIRECTION;
        //一个方向连续插入的记录数量
        public short PAGE_N_DIRECTION;
        //该页中用户记录的数量
        public short PAGE_N_RECS;
        //修改当前页的最大事务id,该值仅在二级索引页面中定义
        public long PAGE_MAX_TRX_ID;
        //当前页在B+树中所处的层级(从0层开始
        public short PAGE_LEVEL;
        //索引id,表示当前页属于哪个索引
        public long PAGE_INDEX_ID;
        //B+树叶子节点段的头部信息
        public long PAGE_BTR_SEF_LEAF;
        //B+树非叶子节点段的头部信息
        public long PAGE_BTR_SEG_TOP;
    }

    /*//用户记录
    public static class UserRecords implements Serializable{
        public List<Compact> records = new ArrayList<>();

        public List<Compact> getRecords() {
            return records;
        }
    }*/

    //compact行格式
    public static class Compact implements Serializable{
        //记录的额外信息
        //1、变长字段长度列表
        List<Integer> variableList = new ArrayList<>();
        //2、NULL值列表
        List<Integer> nullList = new ArrayList<>();
        //3、记录头信息
        public RecordingHeader recordingHeader = new RecordingHeader();

        public RecordingHeader getRecordingHeader() {
            return recordingHeader;
        }

        public List<Integer> getVariableList() {
            return variableList;
        }

        public List<Integer> getNullList() {
            return nullList;
        }
    }
    //记录头信息
    public static class RecordingHeader implements Serializable{
        //预留位1
        public byte reservedSpace1;
        //预留位2
        public byte reservedSpace2;
        //删除标记(0未删除,1已删除)
        public byte deletedFlag;
        //最小的目录项记录
        public byte minRecFlag;
        //页面分组后的组中记录条数
        public int nOwned;
        //记录在当前页面堆中的编号(相对位置 0代表页面中的最大记录)
        public long heapNo;
        //当前的记录类型(0:普通记录 1:B+树非叶子结点的目录项记录 2:表示infimum记录 3:表示supremum记录)
        public int recordType;
        //表示下一条记录的相对位置
        public long nextRecord;
        /**
         * 具有两个隐藏列,tx_id,roll_pointer(如果表没有主键还会添加一个row_id列)
         */
        public List<String> colValues = new ArrayList<>();

        public List<String> getColValues() {
            return colValues;
        }
    }

    /**
     * 页目录
     * 记录在页中按主键值从小到大的顺序串联为一个单向链表
     * 将非删除的数据(包含infimum和supremum记录)划分为几个组
     * 1、对于infimum记录所在分组只能有1条记录
     * 2、对于supremum记录所在分组只能在1-8条记录之间
     * 3、剩下的记录所在分组只能在4-8条记录直接
     * 分组步骤
     * 1、初始情况下，一个数据页中只有Infimum记录和Supremum记录这两条，所以分为两个组。
     * 2、之后每当插入一条记录时，都会从页目录中找到对应记录的主键值比待插入记录的主键值大，并且差值最小的槽，然后把该槽对应的n_owned加1。
     * 3、当一个组中的记录数等于8时，当再插入一条记录的时候，会将组中的记录拆分成两个组（一个组中4条记录，另一个组中5条记录）。并在拆分过程中，会在Page Directory中新增一个槽，并记录这个新增分组中最大的那条记录的偏移量。
     * 4、每个组的最后一条记录（即：也是这个组里，最大的那条记录）——“带头大哥”，其余的记录均为"组内小弟"；"大哥"记录的头信息中的n_owned属性表示该组内共有几条记录，而"小弟"的n_owned属性都为0；
     * 5、将"大哥"在页面中的地址偏移量取出来，按顺序存储到靠近Page Trailer的地方。这个地方就是Page Directory。
     * 6、Page Directory中的这些地址偏移量被称为槽（Slot），每个槽占用2个字节。一个正常的页面为16KB，即：16384字节。而2个字节可以表示的地址偏移量范围是0～(2^16-1)，即：0～65535。所以2个字节表示一个槽足够了。
     * 查找：
     * 第一步：通过二分法确定该记录所在分组对应的Slot，然后找到该Slot所在分组中主键值最小的那条记录。
     *      每个槽对应的都是组内主键值最大的记录，那么怎么定位一个组中主键值最小的记录呢？答：由于每个槽都是挨着的，
     *      所以，我们可以通过找到前一个槽中的最大主键值记录，这个记录的下一条记录（next_record），就是本槽的最小主键值记录。
     * 第二步：通过记录的next_record属性遍历该槽所在组中的各个记录。
     *  由于每个槽都是挨着的，所以，我们可以通过找到前一个槽中的最大主键值记录，这个记录的下一条记录（next_record），就是本槽的最小主键值记录。
     */
    public static class PageDirectory implements Serializable{
        public List<Slot> slotList = new ArrayList<>();

        public List<Slot> getSlotList() {
            return slotList;
        }

        //存有1-8条记录的分组 -槽
        public static class Slot implements Serializable{
            public List<Compact> records = new ArrayList<>();

            public List<Compact> getRecords() {
                return records;
            }
        }
    }

    //文件尾部
    public static class FileTrailer implements Serializable{
        public int checksum;
        public int LSN;
    }

    public Compact getInfimum() {
        return infimum;
    }

    public Compact getSupremum() {
        return supremum;
    }

    public FileHeader getFileHeader() {
        return fileHeader;
    }

    public PageHeader getPageHeader() {
        return pageHeader;
    }

    /*public UserRecords getUserRecords() {
        return userRecords;
    }*/

    public PageDirectory getPageDirectory() {
        return pageDirectory;
    }

    public FileTrailer getFileTrailer() {
        return fileTrailer;
    }

    public void setInfimum(Compact infimum) {
        this.infimum = infimum;
    }

    public void setSupremum(Compact supremum) {
        this.supremum = supremum;
    }
}
