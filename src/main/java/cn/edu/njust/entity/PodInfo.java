package cn.edu.njust.entity;

import java.util.List;

/**
 * @author TYX
 * @name PodInfo
 * @description
 * @createTime 2021/6/25 14:57
 **/
public class PodInfo {
    private String namespace;
    private String name;
    private String status;
    private String age;
    private String containers; //理论上只有一个容器

    public PodInfo(String namespace, String name, String status, String age, String containers) {
        this.namespace = namespace;
        this.name = name;
        this.status = status;
        this.age = age;
        this.containers = containers;
    }

    @Override
    public String toString() {
        return "PodInfo{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", age='" + age + '\'' +
                ", containers='" + containers + '\'' +
                '}';
    }
}
