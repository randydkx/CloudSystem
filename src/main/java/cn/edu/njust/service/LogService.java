package cn.edu.njust.service;

import cn.edu.njust.entity.Request;
import cn.edu.njust.utils.kubenetecontrol.*;
import cn.edu.njust.utils.publicclouds.ClusterManager;
import cn.edu.njust.utils.publicclouds.Deployment;
import cn.edu.njust.utils.publicclouds.VMType;
import cn.edu.njust.utils.scheduler.IDataCenterScheduler;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author TYX
 * @name LogService
 * @description
 * @createTime 2021/7/15 21:03
 **/
public class LogService {


    public static List<Request> getRequests() {
        LogUtil logmanager = new LogUtil(new ControlCenter());
//        Thread t = new Thread(logmanager);
//        t.start();
        logmanager.readLogs();
        System.out.println("finish reading log");
        int index = 1;
        for(Request request : logmanager.requestList){
            System.out.println(index++);
            System.out.println(request);
        }
        return logmanager.requestList;
    }

    public static void main(String[] args) {
        List<Request> requestList = getRequests();
    }
}
