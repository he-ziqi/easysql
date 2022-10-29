package world.hzq.easysql.test;

import world.hzq.easysql.storage.engine.datastruct.BPlusTree;
import java.util.List;

public class BPlusTreeTest {
    public static void main(String[] args) {
        BPlusTree<Integer,Integer> tree = new BPlusTree<>();
        for (int i = 0; i < 1000000; i++) {
            tree.insert(i,i + 1);
        }
        List<Integer> list = tree.rangeQuery(0, 10000);
        if (list.size() > 0) {
            list.forEach(System.out::println);
        }else {
            System.out.println("空集合");
        }
        /*System.out.println("=====================================================================");
        BPlusTree<String,Integer> btree = new BPlusTree<>();
        List<String> ids = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            String s = UUID.randomUUID().toString();
            ids.add(s);
            btree.insert(s,i);
        }
        *//*for (int i = 0; i < 20; i++) {
            btree.query(ids.get(i)).forEach(System.out::println);
        }*//*
        List<Integer> list = btree.rangeQuery(ids.get(0), ids.get(19));
        list.forEach(System.out::println);*/
    }
}
