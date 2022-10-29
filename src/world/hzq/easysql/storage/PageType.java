package world.hzq.easysql.storage;

public enum PageType {
    FIL_PAGE_INDEX(0x45BF), //数据页
    FIL_PAGE_TYPE_SYS(0x0006), //系统页
    FIL_PAGE_UNDO_LOG(0x0002), //Undo log页
    FIL_PAGE_TYPE_BLOB(0x000A), //BLOB页
    FIL_PAGE_TYPE_TRX_SYS(0x0007), //事务系统数据
    FIL_PAGE_TYPE_ALLOCATED(0x0000); //最新分配页
    private final short val;

    public short getVal(){
        return this.val;
    }

    PageType(final int val){
        this.val = (short) val;
    }
}
