package world.hzq.easysql;

import world.hzq.easysql.conn.Connection;

import java.io.IOException;

/**
 * create database easysql;
 * use easysql;
 * create table user(id int primary key auto_increment,name varchar(20),age int);
 * insert into user(id,name,age) values(1,'张三',20),(2,'李四',30),(3,'王五',25),(4,'zs',18),(5,'lisi',35);
 */
public class AppStart {
    public static void main(String[] args) throws IOException {
        new Connection().start();
    }
}
