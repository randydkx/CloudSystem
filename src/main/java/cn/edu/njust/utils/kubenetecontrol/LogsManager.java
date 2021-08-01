package cn.edu.njust.utils.kubenetecontrol;

/*
Copyright 2017 The Kubernetes Authors.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import cn.edu.njust.entity.Request;
import cn.edu.njust.entity.Response;
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

//import scheduler.UnequalMMNSchedulingAlgorithm;
//import java.util.BlockingQueue;

public class LogsManager implements Runnable{
    ControlCenter controllercenter=null;

    public LogsManager(ControlCenter controllercenter) {
        super();
        this.controllercenter = controllercenter;
    }
    //static ArrayList<Double> response_temp=new ArrayList<Double>();
    private LinkedList<String> queue_processedrequests=new LinkedList<String>();
    //private String currenttime=null;
    private LinkedList<String> open_minutes=new LinkedList<String>();

    private LinkedList<NginxStatus> open_nginxstatus=new LinkedList<NginxStatus>();

    private HashMap<String, ArrayList<Double>> responsetime_temp=new HashMap<String, ArrayList<Double>>();
    private HashMap<String, Double> error_temp=new HashMap<String, Double>();
    private ArrayList<MinuteRequests> requestshistory=new ArrayList<MinuteRequests>();

    private Date latestminutes=null;
    //private HashMap<String, Double> responsetimes=new HashMap<String, Double>();
    //private HashMap<String, Double> arrivedrequests=new HashMap<String, Double>();
    private boolean executing=true;

    public String latestlogfile=null;
    public void setExecuting(boolean executing) {
        this.executing = executing;
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        readLogs();

    }

    /**
     * Get arrival rates
     * @param steps 1 means current
     * @return
     */
    public ArrayList<Double> getArrivalRates(int steps)
    {
        //ArrayList<MinuteRequests> histories=
        synchronized(open_nginxstatus) {
            //critical section


            ArrayList<Double> arrivalrates=new ArrayList<Double>();
            int historylength=open_nginxstatus.size();
            double lastacceptedsessions=-1;
            Date lastdate=null;
            for(int i=steps+1;i>0;i--)
            {
                int index=historylength-i;
                if(index<0)
                    break;
                NginxStatus temp=open_nginxstatus.get(index);
                if(lastacceptedsessions==-1)
                {
                    lastacceptedsessions=temp.accepts_connections;
                    lastdate=temp.date;
                }
                else
                {
                    double arrivalrate=temp.accepts_connections-lastacceptedsessions;
                    double interval=(temp.date.getTime()-lastdate.getTime())/1000d;
                    if(interval!=0)
                    {
                        arrivalrate=arrivalrate/interval;
                        //arrivalrate=SystemParameter.averagerequesteachuser*arrivalrate;
                        arrivalrates.add(arrivalrate);
                    }
                }
            }
            Log.printLine("arrivalrates:"+arrivalrates);
            return arrivalrates;
        }


    }

    /*
     * Get average response times
     */
    public ArrayList<Double> getResponseTimes(int steps)
    {
        synchronized(open_minutes) {
            ArrayList<Double> responsetimes=new ArrayList<Double>();
            if(open_minutes.size()<=0||open_nginxstatus.size()<=0)
            {
                return null;
            }
            int historylength=requestshistory.size();

            for(int i=steps;i>0;i--)
            {
                int index=historylength-i;
                if(index>=0)
                    responsetimes.add(requestshistory.get(index).responsetime);
            }

            int index=0;
            //int lastsize=-1;
            while(index<open_minutes.size()-1)
            {
                String oldkey=open_minutes.get(index);

                ArrayList<Double> oldresponsetimes=responsetime_temp.get(oldkey);
                //NginxStatus status = open_nginxstatus.get(index);
                double totaltime=0;
                for(int i=0;i<oldresponsetimes.size();i++)
                {
                    totaltime+=oldresponsetimes.get(i);
                }
                double averageresponsetime=-1;
                //if(lastsize!=0&&oldresponsetimes.size()<lastsize*0.3)// too few samples
                //{
                //	index++;
                //	continue;
                //}
                if(oldresponsetimes.size()>0)
                    averageresponsetime=totaltime/oldresponsetimes.size();
                responsetimes.add(averageresponsetime);
                //lastsize=oldresponsetimes.size();

                index++;
            }
            while(responsetimes.size()>steps)
            {
                responsetimes.remove(0);
            }
            Log.printLine("getResponseTimes(int steps):"+responsetimes.toString());
            return responsetimes;
        }
    }

    /**
     * including processing
     * @return
     */
    public ArrayList<Double> getQueuelength()
    {
        ArrayList<Double> queuelengthlist=new ArrayList<Double>();

        synchronized(open_nginxstatus) {
            if(open_nginxstatus.size()-2>=0)
                queuelengthlist.add(open_nginxstatus.get(open_nginxstatus.size()-2).Active_connections);
            else
            {
                if(requestshistory.size()-1>=0)
                    queuelengthlist.add(requestshistory.get(requestshistory.size()-1).nginxstatus.Active_connections);
            }
            if(open_nginxstatus.size()-1>=0)
                queuelengthlist.add(open_nginxstatus.get(open_nginxstatus.size()-1).Active_connections);
            NginxStatus status=readNginxStatus();
            if(status!=null)
            {
                queuelengthlist.add(status.Active_connections);
            }
            Log.printLine("getQueuelength():"+queuelengthlist.toString());
        }
        return queuelengthlist;
    }

    public ArrayList<MinuteRequests> getRequestshistory() {
        return requestshistory;
    }

    /**
     * Read the status information of nginx
     */
    private NginxStatus readNginxStatus()
    {

        URL url;
        while(true)
        {
            NginxStatus status=new NginxStatus();
            status.date=new Date();
            BufferedReader reader=null;
            InputStreamReader inputreader=null;
            InputStream instr=null;
            try {
                url = new URL(SystemParameter.nginxstatusurl);
                instr=url.openStream();
                inputreader=new InputStreamReader(instr);
                reader = new BufferedReader(inputreader);
                String s;
                int index=-1;
                while ((s = reader.readLine()) != null)
                {
                    Log.printLine(s);
                    if(s.contains("Active connections:"))
                    {
                        String activeconnections=s.replace("Active connections:", "").trim();
                        status.Active_connections=Double.valueOf(activeconnections);
                        index=1;
                    }
                    else if(index==1)
                    {
                        index++;
                    }
                    else if(index==2)
                    {
                        String[] items=s.split(" ");
                        if(items.length==4)
                        {
                            status.accepts_connections=Double.valueOf(items[1]);
                            status.handled_connections=Double.valueOf(items[2]);
                            status.handled_requests=Double.valueOf(items[3]);
                        }
                        else
                        {
                            Log.printLine("The format of nginx status is not right");
                        }
                        index++;
                    }
                    else if(index==3)
                    {

                    }
                }
                inputreader.close();
                reader.close();
                instr.close();
                if(index>0)
                {
                    Log.printLine(status);
                    return status;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.printLine("Refetch status.....................");
            } catch (IOException e) {
                // TODO Auto-generated catch block
//				在这里产生了connect refused error
                Log.printLine(e.getMessage()+" Refetch status.....................");

                try {
                    if(inputreader!=null)
                        inputreader.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }

                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }

                try {
                    if(instr!=null)
                        instr.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }


    }
    private long count=0;
    private long maxcount=60000;
    public void readLogs()
    {

        /*
         * String kubeConfigPath = "D:\\workspace\\KubenetControl\\config"; ApiClient
         * client=null; // loading the out-of-cluster config, a kubeconfig from
         * file-system try { client =
         * ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new
         * FileReader(kubeConfigPath))).build(); // set the global default api-client to
         * the in-cluster one from above //Configuration.setDefaultApiClient(client);
         *
         * // the CoreV1Api loads default api-client from global configuration.
         * Log.printLine("Log Connection established"); } catch (IOException e)
         * {//| ApiException // TODO Auto-generated catch block e.printStackTrace(); }
         */

        CoreV1Api coreApi = Connect2System.getAPI();

        Connect2System.client.setReadTimeout(30000);

        PodLogs logs = new PodLogs();
        V1Pod pod=null;
        try {
            pod = coreApi
                    .listNamespacedPod("ingress-nginx", "false", null, null, null, null, null, null, null, null)
                    .getItems()
                    .get(0);
            Log.printLine("***********************listNamespacedPod********************");
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
                            Log.printLine("We are still trying to read Logs.....");
                        }
                        text = br.readLine();

                        Log.printLine(text);
                        processAlog(text);
                        if(count>maxcount)
                        {
                            Log.printLine("We successfully read 60000 Logs.....");
                            Log.printLine(text);
                            count=0;
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Log.printLine(text);
                        Log.printLine("br.readLine()"+e.getMessage());
                        br.close();
                        isr.close();
                        is.close();
                        //Connect2System.client.
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        Log.printLine("======================Try to reopen is = logs.streamNamespacedPodLog(pod);2");
                        is = logs.streamNamespacedPodLog(pod);
                        isr=new InputStreamReader(is);

                        br=new BufferedReader(isr);
                        Log.printLine("======================Finish reopen is = logs.streamNamespacedPodLog(pod);2");
                        count=maxcount;
                        controllercenter.readingoldlog=true;
                    }
                }
            } catch (IOException | ApiException e1) {
                // TODO Auto-generated catch block
                Log.printLine("readLogs()1"+e1.getMessage());
                try {

                    br.close();
                    isr.close();
                    is.close();
                    Thread.sleep(5000);
                } catch (InterruptedException | IOException e) {
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
     * @param log
     */
    private void processAlog(String log)
    {
        if(count>maxcount)
        {
            Log.printLine("Enter processAlog "+log);
        }
        if(log.indexOf("AccessLog")!=0)
        {
//		 处理包含错误的log信息
            if(log.contains("[error]"))
            {
                try {
                    if(count>maxcount)
                    {
                        Log.printLine("processErrorLogs ");
                    }
                    processErrorLogs(log);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return;
        }
        // Log.printLine(log);
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
//        SystemParameter.requestList.add(request);
        if(count>maxcount)
        {
            Log.printLine("processLogs before SimpleDateFormat dateFormat");
        }
        //Date mdate=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.UK);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        //Log.printLine(dateFormat.format(mdate));
        try {

            //logtime=logtime.replace(" +0000", "");
            Date date=dateFormat.parse(logtime);

            SimpleDateFormat destdateFormat=new SimpleDateFormat("dd/MMM/yyyy:HH:mm Z", Locale.UK);
            destdateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String timekey=destdateFormat.format(date);
            if(controllercenter.readingoldlog)
            {
                if(count>maxcount)
                {
                    Log.printLine("processLogs old logs..."+log);
                }
                Date currenttime=new Date();
                long gap=currenttime.getTime()-date.getTime();
                if(gap<60*1000*20)
                {
                    controllercenter.readingoldlog=false;
                    Log.printLine("Set controllercenter.readingoldlog=false");
                }
                return;
            }
            if(count>maxcount)
            {
                Log.printLine("processLogs before queue_processedrequests.contains(reqid)"+log);
            }
            if(queue_processedrequests.contains(reqid))//this request has been processed
            {
                if(count>maxcount)
                {
                    Log.printLine("queue_processedrequests.contains(reqid) return ");
                }
                return;
            }

            queue_processedrequests.add(reqid);
            if(queue_processedrequests.size()>100000)
            {
                queue_processedrequests.poll();
            }

            if (open_minutes.contains(timekey))//if this minute has been added to open_minutes
            {
                if(count>maxcount)
                {
                    Log.printLine("open_minutes.contains(timekey)");
                }
                ArrayList<Double> responsetimes= responsetime_temp.get(timekey);
                responsetimes.add(Double.valueOf(responsetime));
            }
            else //this is a new minuete
            {
                Log.printLine("Coming to a new minute...");
                Log.printLine(log);
                if(latestminutes==null)
                {
                    latestminutes=date;
                    Log.printLine("initial latestminutes=date="+latestminutes.toString());
                }
                else
                {
                    if(date.before(latestminutes))//this new minutes is before the latest minute
                    {
                        Log.printLine("date.before(latestminutes) return latestminutes="+latestminutes.toString());
                        return;
                    }
                    else
                    {
                        latestminutes=date;
                        Log.printLine("latestminutes=date="+latestminutes.toString());
                    }
                }



                ArrayList<Double> responsetimes =new ArrayList<Double>();
                responsetimes.add(Double.valueOf(responsetime));
                synchronized(open_minutes) {
                    responsetime_temp.put(timekey, responsetimes);
                    error_temp.put(timekey, 0d);
                    open_minutes.add(timekey);
                }
                /**
                 * Read status
                 */
                NginxStatus status=readNginxStatus();
                synchronized(open_nginxstatus) {
                    open_nginxstatus.add(status);
                }
                updateOpenminutes();


            }

            //Log.printLine(timekey);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void processErrorLogs(String log)
    {
        //Log.printLine(log);
        // log="2020/04/12 06:43:42 [error] 42#42: *91364 recv() failed (104: Connection reset by peer) while reading response header from upstream, client: 10.244.0.1, server: myrubis.czc, request: \"GET /Count/?tel=30 HTTP/1.1\", upstream: \"http://10.244.1.14:8080/Count/?tel=30\", host: \"myrubis.czc:31597\"";
        String logtime=log.substring(0, 20);
        int idindex=log.indexOf("*");
        int idendindex=-1;
        if(idindex<0)
            return;
        try {
            for(int i=idindex;i<log.length();i++)
            {
                if(log.charAt(i)==' ')
                {
                    idendindex=i;
                    break;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String reqid=log.substring(idindex, idendindex);
        //Log.printLine(reqid);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.UK);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        //	Log.printLine(dateFormat.format(log));


        logtime=logtime+" +0000";
        Date date=null;
        try {
            date = dateFormat.parse(logtime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(controllercenter.readingoldlog)
        {
            Date currenttime=new Date();
            long gap=currenttime.getTime()-date.getTime();
            if(gap<60*1000*5)
            {
                controllercenter.readingoldlog=false;
                Log.printLine("Set controllercenter.readingoldlog=false");
            }
            return;
        }
        SimpleDateFormat destdateFormat=new SimpleDateFormat("dd/MMM/yyyy:HH:mm Z", Locale.UK);
        destdateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timekey=destdateFormat.format(date);

        if(queue_processedrequests.contains(reqid))//this request has been processed
        {
            return;
        }

        queue_processedrequests.add(reqid);
        if(queue_processedrequests.size()>100000)
        {
            queue_processedrequests.poll();
        }

        if (open_minutes.contains(timekey))//if this minute has been added to open_minutes
        {
            double errorcount=error_temp.get(timekey);
            error_temp.put(timekey, errorcount+1);
        }
        else //this is a new minuete
        {
            if(latestminutes==null)
            {
                latestminutes=date;
            }
            else
            {
                if(date.before(latestminutes))//this new minutes is before the latest minute
                {
                    return;
                }
                else
                {
                    latestminutes=date;
                }
            }
            Log.printLine(log);
            ArrayList<Double> responsetimes =new ArrayList<Double>();
            responsetime_temp.put(timekey, responsetimes);
            open_minutes.add(timekey);
            error_temp.put(timekey, 1d);

            /**
             * Read status
             */
            NginxStatus status=readNginxStatus();
            open_nginxstatus.add(status);

            updateOpenminutes();


        }
    }
    private void updateOpenminutes()
    {
        if(open_minutes.size()>3) //remove the record of 3 minuetes ago
        {
            String oldkey=open_minutes.poll();

            ArrayList<Double> oldresponsetimes=responsetime_temp.remove(oldkey);
            NginxStatus status = open_nginxstatus.poll();
            double totaltime=0;
            for(int i=0;i<oldresponsetimes.size();i++)
            {
                totaltime+=oldresponsetimes.get(i);
            }
            double averageresponsetime=-1;
            if(oldresponsetimes.size()>0)
                averageresponsetime=totaltime/oldresponsetimes.size();

            MinuteRequests reqdata=new MinuteRequests();
            reqdata.processedrequests=oldresponsetimes.size();
            reqdata.responsetime=averageresponsetime;
            reqdata.time=oldkey;
            reqdata.errorrequests=error_temp.get(oldkey);
            reqdata.nginxstatus=status;

            reqdata.vmcapacity=this.controllercenter.fibanaci_deployment.containernum*this.controllercenter.fibanaci_deployment.vmtype.processability;
            reqdata.totalrentalcost=this.controllercenter.clustermanager.getContainerCost();

            Log.printLine("Waiting to enter synchronized updateOpenminutes");
            synchronized(requestshistory) {
                Log.printLine("endtered to synchronized updateOpenminutes");

                this.requestshistory.add(reqdata);

                Log.printLine("Leave synchronized updateOpenminutes");
            }
            String message="time:"+reqdata.time+"-processedrequests:"+reqdata.processedrequests+"-responsetime:"+reqdata.responsetime+"-errorrequests:"+reqdata.errorrequests+",totalaccepts_connections"+reqdata.nginxstatus.accepts_connections;
            Log.printLine(message);
            //displayRecords();
        }
    }
    private void displayRecords()
    {
        double lastacceptedsessions=0;
        Date lastdate=null;
        for(int i=0;i<requestshistory.size();i++)
        {
            MinuteRequests temp=requestshistory.get(i);

            double arrivalrate=-1;
            if(lastdate!=null)
            {
                arrivalrate=temp.nginxstatus.accepts_connections-lastacceptedsessions;
                double interval=(temp.nginxstatus.date.getTime()-lastdate.getTime())/1000;
                if(interval!=0)
                {
                    arrivalrate=arrivalrate/interval;
                    //arrivalrate=SystemParameter.averagerequesteachuser*arrivalrate;

                }
                lastacceptedsessions=temp.nginxstatus.accepts_connections;
                lastdate=temp.nginxstatus.date;
            }
            else
            {
                lastacceptedsessions=temp.nginxstatus.accepts_connections;
                lastdate=temp.nginxstatus.date;
            }
            String message="time:"+temp.time+"-processedrequests:"+temp.processedrequests+"-responsetime:"+temp.responsetime+"-errorrequests:"+temp.errorrequests+",totalaccepts_connections"+temp.nginxstatus.accepts_connections;
            message+="-arrivalnewconnections:"+arrivalrate;
            if(i==requestshistory.size()-1)
                Log.printLine(message);
        }

    }
    public String SaveLogstoJson()
    {
        Date date=new Date();
        String datestr=date.toString().replace(":", "-");
        switch (SystemParameter.controltype) {
            case MMK_Adjust:

                datestr+="MMK_Adjust";
                break;

            case MMKForward_FeedbackOriginal:
                datestr+="MMKForward_FeedbackOriginal";
                break;
            case MMK_Unequal:
                datestr+="MMK_Unequal";
                break;
            case FeedBack_Inverse:
                datestr+="FeedBack_Inverse";
                break;
            case FeedBack_MMK:
                datestr+="FeedBack_MMK";
                break;
            default:
                break;
        }

        String outputfilename=SystemParameter.database+"\\logs\\Log-"+datestr+".json";
        outputfilename=outputfilename.replace(" ", "");
        File outputfile=new File(outputfilename);
        if(!outputfile.exists())
        {
            try
            {
                outputfile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                outputfile.delete();
                outputfile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            JsonGenerator generator= Json.createGenerator(new FileOutputStream(outputfile));
            generator.writeStartObject();

            generator.writeStartArray("Latency");

            double lastacceptedsessions=0;
            Date lastdate=null;
            Log.printLine("Waiting to enter synchronized SaveLogstoJson");
            synchronized(requestshistory) {
                Log.printLine("endtered to synchronized SaveLogstoJson");
                for(int i=0;i<requestshistory.size();i++)
                {
                    MinuteRequests temp=requestshistory.get(i);

                    double arrivalrate=-1;
                    if(lastdate!=null)
                    {
                        arrivalrate=temp.nginxstatus.accepts_connections-lastacceptedsessions;
                        long interval=(temp.nginxstatus.date.getTime()-lastdate.getTime())/1000;
                        if(interval!=0)
                        {
                            arrivalrate=arrivalrate/interval;
                            //arrivalrate=SystemParameter.averagerequesteachuser*arrivalrate;
                        }
                        lastacceptedsessions=temp.nginxstatus.accepts_connections;
                        lastdate=temp.nginxstatus.date;
                        if(interval<30)//reading historical data
                            continue;
                    }
                    else
                    {
                        lastacceptedsessions=temp.nginxstatus.accepts_connections;
                        lastdate=temp.nginxstatus.date;
                        continue;// the first data is not stored
                    }
                    String message="time:"+temp.time+"-processedrequests:"+temp.processedrequests+"-responsetime:"+temp.responsetime+"-errorrequests:"+temp.errorrequests+",totalaccepts_connections"+temp.nginxstatus.accepts_connections;
                    message+="-arrivalnewconnections:"+arrivalrate;
                    if(i==requestshistory.size()-1)
                        Log.printLine(message);
                    generator.writeStartObject();
                    generator.write("time", temp.time);
                    generator.write("processedrequests", temp.processedrequests);
                    generator.write("responsetime", temp.responsetime);
                    generator.write("errorrequests", temp.errorrequests);
                    generator.write("accepts_connections", temp.nginxstatus.accepts_connections);
                    generator.write("vmcapacity", temp.vmcapacity);
                    generator.write("totalrentalcost", temp.totalrentalcost);//cost from starting
                    generator.write("arrivalnewconnections", arrivalrate);//cost from starting
                    generator.writeEnd();
                }
            }
            Log.printLine("Leaved synchronized SaveLogstoJson");
            generator.writeEnd();
            generator.writeEnd();
            generator.close();
            latestlogfile=outputfilename;
            return outputfilename;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }


}