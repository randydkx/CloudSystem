package cn.edu.njust.entity;

import java.sql.Timestamp;

/**
 * @author TYX
 * @name Response
 * @description 请求的回应
 * @createTime 2021/6/25 14:25
 **/
public class Response {
    private Request request;
    private Timestamp responseTime;
    private String status;
}
