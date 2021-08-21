package cn.edu.njust.utils.linuxshell;

public class LinuxDataBase {
    private String username;
    private String password;
    private String url;
    private String port;

    public LinuxDataBase(String username, String password, String url, String port) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.port = port;
    }

    public LinuxDataBase(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
