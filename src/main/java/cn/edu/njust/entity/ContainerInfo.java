package cn.edu.njust.entity;

/**
 * @author TYX
 * @name ContainerInfo
 * @description
 * @createTime 2021/6/25 14:57
 **/
public class ContainerInfo {
    private String name;
    private String image;

    public ContainerInfo(String name, String image) {
        this.name = name;
        this.image = image;
    }

    @Override
    public String toString() {
        return "ContainerInfo{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
