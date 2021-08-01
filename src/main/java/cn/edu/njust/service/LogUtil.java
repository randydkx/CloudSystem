package cn.edu.njust.service;

import cn.edu.njust.entity.Request;
import cn.edu.njust.utils.kubenetecontrol.*;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogUtil{
    ControlCenter controllercenter=null;
    public static List<Request> requestList = new ArrayList<Request>();

    public LogUtil(ControlCenter controllercenter) {
        super();
        this.controllercenter = controllercenter;
    }
    private boolean executing=true;

    private long count=0;
    private long maxcount=60000;
    public void readLogs()
    {
        requestList.clear();

        CoreV1Api coreApi = Connect2System.getAPI();

        Connect2System.client.setReadTimeout(100);

        PodLogs logs = new PodLogs();
        V1Pod pod=null;
        try {
            pod = coreApi
                    .listNamespacedPod("ingress-nginx", "false", null, null, null, null, null, null, null, null)
                    .getItems()
                    .get(0);
        } catch (ApiException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        InputStream is=null;
        InputStreamReader isr=null;
        BufferedReader br=null;

        while(executing)
        {
            try {
                Log.printLine("======================open is = logs.streamNamespacedPodLog(pod);1");
                is = logs.streamNamespacedPodLog(pod);

                isr=new InputStreamReader(is);

                br=new BufferedReader(isr);
                Log.printLine("======================Finish open is = logs.streamNamespacedPodLog(pod);1");
                while(executing)
                {
                    String text="";
                    try {
                        count++;
                        if(count>maxcount)
                        {
                            executing=false;
                        }
                        text = br.readLine();

                        processAlog(text);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Log.printLine(text);
                        Log.printLine("br.readLine()"+e.getMessage());
                        br.close();
                        isr.close();
                        is.close();
                        System.out.println("close stream!!!");
                        return;
                    }
                }
            } catch (IOException | ApiException e1) {
                // TODO Auto-generated catch block
                Log.printLine("readLogs()1"+e1.getMessage());
                try {

                    br.close();
                    isr.close();
                    is.close();

                    return ;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.printLine("readLogs()2"+e.getMessage());
                }
            }
        }
        Log.printLine("Exit reading logs!!!!!!!!!!!!!!!!!!!!!!!!!");

    }



    /**
     * AccessLog --- R-addr=192.168.83.5 --- Time=09/Apr/2020:02:09:01 +0000 --- Body_bytes_send=649
     * --- status=200 --- requesttime=0.068 --- upstreamaddr=10.244.0.94:80 ---upstreamlength=649
     * --- upstreamresponsetime=0.068 --- httpreferer=http://myrubis.czc/PHP/register.html
     * 处理一条请求日志
     */
    private void processAlog(String log)
    {
//        只处理AccessLog类型的日志，提取请求信息
        if(log.indexOf("AccessLog")!=0)return;


        String[] logs=log.split("---");
        String logtime=null;
        String responsetime=null;
        String reqid=null;
        Request request = new Request();
        for(int i=0;i<logs.length;i++)
        {
            String templogitem=logs[i].trim();
            String[] items=templogitem.split("=");
            if(items.length==2)
            {
//                System.out.println(items[0]);
                if(items[0].equalsIgnoreCase("Time"))
                {
//			  	记录日志时间：格式是01/Aug/2021:07:07:35 +0000
                    logtime=items[1];
                    request.setRequestTime(logtime);
                }
                else if(items[0].equalsIgnoreCase("requesttime"))
                {
//			  	请求时间
                    responsetime=items[1];
                    request.getResponse().setResponseTime(responsetime);
                }
                else if(items[0].equalsIgnoreCase("reqid"))
                {
                    reqid=items[1];
                    request.setReqID(reqid);
                }
                else if(items[0].equalsIgnoreCase("R-addr")){
                    request.setRequestFromAddr(items[1]);
                }
                else if(items[0].equalsIgnoreCase("status")){
                    request.getResponse().setStatus(items[1]);
                }
                else if(items[0].equalsIgnoreCase("upstreamaddr")){
                    request.setRequestToAddr(items[1]);
                }
                else if(items[0].equalsIgnoreCase("upstreamresponsetime")){
                    request.getResponse().setResponseTime(items[1]);
                }
                else if(items[0].equalsIgnoreCase("Body_bytes_send")){
                    request.setBodySendLength(items[1]);
                }
            }
        }
        this.requestList.add(request);
//        System.out.println(request);

    }
}
