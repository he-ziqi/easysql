package world.hzq.easysql.test;

import world.hzq.easysql.storage.Page;
import world.hzq.easysql.storage.ReadPageEntity;
import world.hzq.easysql.storage.util.ByteUtil;
import world.hzq.easysql.storage.util.IOUtil;

import java.io.*;

public class TestIO {
    static File file = new File("test.ibd");
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        insert();
        System.out.println("插入数据~查询所有===");
        queryAll();

        System.out.println("更新数据~查询所有===");
        Page page = new Page();
        update(3,page);
        queryAll();
    }
    public static void query(int index){
        ReadPageEntity readPageEntity = IOUtil.read(index, file);
        Page page = ByteUtil.toObject(readPageEntity.getBytes(), readPageEntity.getOffset(), Page.class);
        System.out.println(page);
    }

    public static void queryAll(){
        for (int i = 0; i < 3; i++) {
            query(i);
        }
    }

    public static void update(int index,Page page){
        IOUtil.update(index,ByteUtil.toByteArray(page),file);
    }

    public static void insert(){
        if(file.exists()){
           file.delete();
        }
        Page page1 = new Page();
        page1.infimum = new Page.Compact();
        Page page2 = new Page();
        page2.infimum = new Page.Compact();
        page2.supremum = new Page.Compact();
        Page page3 = new Page();
        IOUtil.write(ByteUtil.toByteArray(page1),file);
        IOUtil.write(ByteUtil.toByteArray(page2),file);
        IOUtil.write(ByteUtil.toByteArray(page3),file);
    }
}
class Student implements Serializable{
    public String name;
    public int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
