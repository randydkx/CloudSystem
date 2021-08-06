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