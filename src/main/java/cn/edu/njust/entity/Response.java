package cn.edu.njust.entity;

import java.sql.Timestamp;

/**
 * @author TYX
 * @name Response
 * @description 请求的回应
 * @createTime 2021/6/25 14:25
 * @param responseTime 对应于请求的响应时间
 **/
public class Response {
    private String responseTime;
    private String status;

    public String getResponseTime() {
        return responseTime;
    }

    public String getStatus() {
        return status;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseTime='" + responseTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
