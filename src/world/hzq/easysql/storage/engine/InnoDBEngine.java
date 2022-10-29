package world.hzq.easysql.storage.engine;

import world.hzq.easysql.storage.engine.datastruct.BPlusTree;

/**
 * InnDB存储结构
 */
public class InnoDBEngine<K extends Comparable<K>,E>{
    private BPlusTree<K,E> tree;

}