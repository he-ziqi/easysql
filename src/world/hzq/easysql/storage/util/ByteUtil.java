package world.hzq.easysql.storage.util;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 字节工具类
 */
public class ByteUtil {
    private ByteUtil(){}

    /**
     * 对象转为byte数组,可指定是否压缩
     */
    public static byte[] toByteArray(Object obj,boolean compress){
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(null,bos);
            close(null,oos);
        }
        if(compress){
            //返回压缩后的字节数组
            return compress(bytes);
        }
        return bytes;
    }

    /**
     * 对象转为byte数组,默认为压缩
     */
    public static byte[] toByteArray(Object obj){
        return toByteArray(obj,true);
    }


    /**
     * 将字节数组转换为指定对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(byte[] bytes,int offset,Class<T> targetClass,boolean uncompress){
        if(uncompress){
            //对字节数组进行解压缩(只有在解压缩的时候才需要进行截取0至偏移量的字节数组进行解压)
            bytes = uncompress(bytes,offset);
        }
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        Object res = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            res = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(bis,null);
            close(ois,null);
        }
        return (T) res;
    }
    /**
     * 将字节数组转换为Java对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(byte[] bytes,int offset,Class<T> targetClass){
        return toObject(bytes,offset,targetClass,true);
    }

    public static Object toObject(byte[] bytes,int offset){
        return toObject(bytes,offset,Object.class,true);
    }
    public static Object toObject(byte[] bytes,int offset,boolean uncompress){
        return toObject(bytes,offset,Object.class,uncompress);
    }

    /**
     * 将字节数组压缩
     */
    public static byte[] compress(byte[] bt){
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gos = null;
        byte[] res = null;
        try {
            bos = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(bos);
            gos.write(bt);
            //在获取压缩字节数组之前要将gos流关闭
            close(null,gos);
            res = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(null,bos);
        }
        return res;
    }

    /**
     * 解压缩字节数组
     */
    public static byte[] uncompress(byte[] bytes,int offset) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        byte[] res = null;
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        GZIPInputStream gis = null;
        try {
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(bytes,0,offset);
            gis = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int readCnt;
            while ((readCnt = gis.read(buffer)) != -1) {
                out.write(buffer, 0, readCnt);
            }
            res = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(gis,null);
            close(in,out);
        }
        return res;
    }

    //关闭流
    private static void close(InputStream in,OutputStream os){
        if(in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(os != null){
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
