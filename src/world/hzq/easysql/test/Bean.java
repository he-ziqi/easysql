package world.hzq.easysql.test;

import java.io.Serializable;

public class Bean implements Serializable {
    private String name;
    private String age;

    public Bean(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
