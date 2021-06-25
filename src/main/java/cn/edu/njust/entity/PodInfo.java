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
    private List<ContainerInfo> containers; //理论上只有一个容器
}
