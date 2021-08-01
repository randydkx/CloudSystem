package cn.edu.njust.entity;

import java.sql.Timestamp;

/**
 * @author TYX
 * @name Request
 * @description 请求的相关信息
 * @createTime 2021/6/25 13:33
 * @param requestFromAddr 请求的发起ip地址R-addr=192.168.83.5中的ip;
 * @param requestToAddr 向某ip的请求，upstreamaddr=10.122.1.16:8080
 * @param requestTime 请求发送至某ip花费的时间
 * @param reqID 请求编号
 * @param response 响应实体
 * @param bodySendLength 发送的请求体字节数 Body_bytes_send=141中的141
 * 注意：kubernetes的request需要通过pod获取 也就是说基本上与pod有绑定关系
 **/
public class Request {
//    private Timestamp arrivalTime;
    private String reqID;
    private String requestFromAddr;
    private String requestToAddr;
    private String requestTime;
    private Response response = new Response();
    private String bodySendLength = null;

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public void setRequestFromAddr(String requestFromAddr) {
        this.requestFromAddr = requestFromAddr;
    }

    public void setRequestToAddr(String requestToAddr) {
        this.requestToAddr = requestToAddr;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setBodySendLength(String bodySendLength) {
        this.bodySendLength = bodySendLength;
    }

    public String getReqID() {
        return reqID;
    }

    public String getRequestFromAddr() {
        return requestFromAddr;
    }

    public String getRequestToAddr() {
        return requestToAddr;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public Response getResponse() {
        return response;
    }

    public String getBodySendLength() {
        return bodySendLength;
    }

    @Override
    public String toString() {
        return "Request{" +
                "reqID='" + reqID + '\'' +
                ", requestFromAddr='" + requestFromAddr + '\'' +
                ", requestToAddr='" + requestToAddr + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", response=" + response +
                ", bodySendLength='" + bodySendLength + '\'' +
                '}';
    }
}
