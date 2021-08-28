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
//    核心数量
    private int coreNum;
//    node的角色，master or node
    private String role;
//    请求用量以及占比,包括CPU和memory
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
                ", coreNum=" + coreNum +
                ", role='" + role + '\'' +
                ", usage=" + usage +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public int getCoreNum() {
        return coreNum;
    }

    public String getRole() {
        return role;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoreNum(int coreNum) {
        this.coreNum = coreNum;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}