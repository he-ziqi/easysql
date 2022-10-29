package world.hzq.easysql.storage;

public class ReadPageEntity {
    public byte[] bytes;
    public int offset;

    public ReadPageEntity() {
    }

    public ReadPageEntity(byte[] bytes, int offset) {
        this.bytes = bytes;
        this.offset = offset;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
