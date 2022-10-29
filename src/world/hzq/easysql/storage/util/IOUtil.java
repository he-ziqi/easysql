package world.hzq.easysql.storage.util;

import world.hzq.easysql.config.Config;
import world.hzq.easysql.storage.Extent;
import world.hzq.easysql.storage.Page;
import world.hzq.easysql.storage.ReadPageEntity;

import java.io.*;
import java.util.*;

public class IOUtil {
    private IOUtil(){}

    /**
     * 序列化对象到指定文件
     */
    public static void writeObject(Object object,File file){
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取指定文件的最后一个序列化对象
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readAllObject(File file,Class<T> targetClass){
        if (!file.isFile()) {
            throw new RuntimeException("read object error \n cause by : current file is not file or not exists");
        }
        ObjectInputStream oos = null;
        List<T> res = null;
        try {
            res = new ArrayList<>();
            oos = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            while (true) {
                res.add((T) oos.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            if(!(e instanceof EOFException)){
                e.printStackTrace();
            }
        }
        return res;
    }
    public static List<Object> readAllObject(File file){
        return readAllObject(file,Object.class);
    }

    /**
     * 从指定文件中反序列对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T readObject(File file,Class<T> targetClass){
        ObjectInputStream ois = null;
        T res = null;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            res = (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static Object readObject(File file){
        return readObject(file,Object.class);
    }

    /**
     * 更新列表页数据
     * @param updateList key为页码,value为更新的值
     */
    public static void updateListPage(Map<Integer,Page> updateList,File file){
        for (Map.Entry<Integer, Page> entry : updateList.entrySet()) {
            Integer index = entry.getKey();
            Page page = entry.getValue();
            updatePage(index,page,file);
        }
    }

    /**
     * 更新指定页码的数据页
     * @param index 页码
     * @param page 数据页
     * @param file 目标文件
     */
    public static void updatePage(int index,Page page,File file){
        byte[] bytes = ByteUtil.toByteArray(page);
        update(index,bytes,file);
    }

    /**
     * 读取指定范围的页数据
     * @param start 开始页码
     * @param end 结束页码(不包含)
     * @param file 目标文件
     */
    public static List<Page> readRangePage(int start,int end,File file){
        List<Page> pages = new ArrayList<>(end - start);
        for (int i = start; i < end; i++) {
            Page page = readPage(i, file);
            pages.add(page);
        }
        return pages;
    }

    /**
     * 读取页数据
     * @param index 页码(从0开始)
     * @param file 目标文件
     */
    public static Page readPage(int index,File file){
        ReadPageEntity pageEntity = read(index, file);
        return ByteUtil.toObject(pageEntity.getBytes(), pageEntity.getOffset(), Page.class);
    }

    /**
     * 写入区数据到指定文件中
     */
    public static void writeExtent(Extent extent,File file){
        List<Page> pageList = extent.getPageList();
        pageList.forEach(page -> {
            writePage(page,file);
        });
    }

    /**
     * 写入页数据到指定文件中
     */
    public static void writePage(Page page,File file){
        byte[] bytes = ByteUtil.toByteArray(page);
        write(bytes,file);
    }

    /**
     * 读取文件中的最后一个数据页
     * 返回null时代表无数据页
     */
    public static Page readLastPage(File file){
        int index = 0;
        ReadPageEntity current = null;
        ReadPageEntity res = null;
        while((current = read(index,file)) != null){
            index++;
            res = current;
        }
        return res == null ? null : ByteUtil.toObject(res.getBytes(),res.getOffset(),Page.class);
    }

    /**
     * 读取文件中的所有配置页
     */
    public static List<Page> readAllPage(File file){
        int index = 0;
        ReadPageEntity current = null;
        List<Page> res = new ArrayList<>();
        while((current = read(index,file)) != null){
            index++;
            res.add(ByteUtil.toObject(current.getBytes(),current.getOffset(),Page.class));
        }
        return res;
    }

    /**
     * 读取一个数据页
     */
    public static ReadPageEntity read(int index, File file){
        DataInputStream dis = null;
        byte[] bytes = new byte[Page.PAGE_SIZE];
        ReadPageEntity readPageEntity = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            //跳转到指定页所在的地址
            dis.skipBytes(index * (Page.PAGE_SIZE + 4));
            //读取指定页的偏移量
            int offset = dis.readInt();
            //读取指定页的字节数组
            dis.readFully(bytes);
            //存在内容时才进行封装返回结果
            if(offset != 0){
                readPageEntity = new ReadPageEntity();
                readPageEntity.setOffset(offset);
                readPageEntity.setBytes(bytes);
            }
        } catch (IOException e) {
            //EOFException 为文件读取结束
            if(!(e instanceof EOFException)){
                e.printStackTrace();
            }
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return readPageEntity;
    }

    /**
     * 写入一个数据页
     * 数据页为16KB固定大小,不足的用0补齐
     */
    public static void write(byte[] bytes,File file){
        if(isIllegalPageByteArray(bytes)){
            throw new RuntimeException("current storage page over max length the max length is 16KB");
        }
        byte[] newBytes;
        //偏移量 代表剩余页空间的大小的起始位置
        int offset = bytes.length;
        if(bytes.length != Page.PAGE_SIZE){
            newBytes = Arrays.copyOf(bytes,Page.PAGE_SIZE);
        }else{
            newBytes = bytes;
        }
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file,true)));
            //写入一个数据页
            dos.writeInt(offset);
            dos.write(newBytes);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(dos != null){
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新数据页
     */
    public static void update(int index,byte[] bytes,File file){
        if(isIllegalPageByteArray(bytes)){
            throw new RuntimeException("current storage page over max length the max length is 16KB");
        }
        byte[] newBytes;
        if(bytes.length != Page.PAGE_SIZE){
            newBytes = Arrays.copyOf(bytes,Page.PAGE_SIZE);
        }else{
            newBytes = bytes;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file,"rw");
            //要更新的页的偏移量
            int offset = bytes.length;
            //跳转到指定页
            raf.skipBytes(index * (Page.PAGE_SIZE + 4));
            raf.writeInt(offset);
            raf.write(newBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(raf != null){
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //获取配置文件
    public static Properties getConfig(){
        FileReader fileReader = null;
        Properties res = null;
        try {
            fileReader = new FileReader(Config.getConfigFileLocation());
            res = new Properties();
            res.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileReader != null){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /**
     * 判断数据页的字节数组是否合法
     * 必须是要小于等于Page.PAGE_SIZE
     */
    private static boolean isIllegalPageByteArray(byte[] bytes){
        return bytes.length > Page.PAGE_SIZE;
    }
}
