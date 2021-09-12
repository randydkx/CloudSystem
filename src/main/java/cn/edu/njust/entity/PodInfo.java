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
    private Usage usage;

    public PodInfo(String namespace, String name, String status, String age, String containers) {
        this.setNamespace(namespace);
        this.setName(name);
        this.setAge(age);
        this.setStatus(status);
        this.setContainers(containers);
    }

    @Override
    public String toString() {
        return "PodInfo{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", age='" + age + '\'' +
                ", containers='" + containers + '\'' +
                ", usage=" + usage +
                '}';
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Usage getUsage(){return usage;}

    public String getAge() {
        return age;
    }

    public String getContainers() {
        return containers;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setContainers(String containers) {
        this.containers = containers;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}
