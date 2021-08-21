package cn.edu.njust.entity;

/**
 * @author TYX
 * @name NodeInfo
 * @description
 * @createTime 2021/6/25 14:59
 **/
public class NodeInfo {
    private String address;
    private String name;
//    核心数量+存储容量
    private int coreNum;
    private double memory;
//    node的角色，master or node
    private String role;
//    当前并未终止的pod数量
    private int nonTerminalPods;
//    请求用量以及占比
    private Usage usage;


    public NodeInfo(String address, String name) {
        this.address = address;
        this.name = name;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}