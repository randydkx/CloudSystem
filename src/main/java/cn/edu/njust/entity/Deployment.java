package cn.edu.njust.entity;

public class Deployment {
    private String name;
    private String ready;
    private int upToDate;
    private int available;
    private String namespace;

    public String getName() {
        return name;
    }

    public String getReady() {
        return ready;
    }

    public int getUpToData() {
        return upToDate;
    }

    public int getAvailable() {
        return available;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReady(String ready) {
        this.ready = ready;
    }

    public void setUpToDate(int upToDate) {
        this.upToDate = upToDate;
    }

    public void setAvailable(int available) {
        this.available = available;
    }


    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String toString() {
        return "Deployment{" +
                "name='" + name + '\'' +
                ", ready='" + ready + '\'' +
                ", upToDate=" + upToDate +
                ", available=" + available +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
