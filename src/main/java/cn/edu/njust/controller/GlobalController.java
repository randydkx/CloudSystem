package cn.edu.njust.controller;

import cn.edu.njust.entity.*;
import cn.edu.njust.service.InfoService;
import cn.edu.njust.service.LogService;
import cn.edu.njust.utils.MainUtils;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.edu.njust.entity.*;
import cn.edu.njust.utils.publicclouds.*;
import cn.edu.njust.utils.kubenetecontrol.*;
import sun.applet.Main;

@Controller
@RequestMapping(value = "/global")
public class GlobalController {
    public double a = 1;
    public InfoService infoService = null;

    GlobalController() throws IOException {infoService = new InfoService(MainUtils.KUBE_CONFIG_PATH);}

    @RequestMapping(value = "/fun")
    public String fun(){
        System.out.println("helloworld");

        CoreV1Api coreApi = Connect2System.getAPI();
        LogService.getRequests();
        return "page/test";
    }

    @RequestMapping(value = "/requestList")
    public  @ResponseBody HashMap<Object, Object>  getRequestList(Model model){
        List<Request> requestList = LogService.getRequests();
        List<String> responseTimeList = new ArrayList<>();
        for(Request item:requestList){responseTimeList.add(item.getResponse().getResponseTime());}
        HashMap<Object,Object> map = new HashMap<Object, Object>();
        map.put("requestList",requestList);
        map.put("responseTimeList",responseTimeList);
        return map;
    }

    /**
     * 资源概况页面中对于整个集群而言CPU和内存的使用率
     * @param model
     * @param session
     * @return
     * @throws ApiException
     */
    @RequestMapping(value = "/clusterCPUMEMUpdate")
    public @ResponseBody Map<Object,Object> ClusterInfoUpdate(Model model, HttpSession session) throws ApiException{
        Map<Object,Object> map = new HashMap<Object, Object>();
        List<NodeInfo> nodeList = infoService.getNodeInfo();
        List<Double> weight = new ArrayList<Double>();
        int nodeNum = nodeList.size();
        for(int i=0;i<nodeNum;i++){weight.add(1.0/(double)nodeNum);}
        double currentCPU = .0;
        double currentMEM = .0;
        for(int i=0;i<nodeNum;i++){
            currentCPU += weight.get(i) * nodeList.get(i).getUsage().getCPURatio();
            currentMEM += weight.get(i) * nodeList.get(i).getUsage().getMemoryRatio();
        }
        double minRandom = - 5;
        double maxRandom = + 5;
        currentCPU += Math.random() * (maxRandom - minRandom) + minRandom;
        currentMEM += Math.random() * (maxRandom - minRandom) + minRandom;
        List<Double> totalCPUListy = (ArrayList<Double>)session.getAttribute("totalCPUListy");
        if (totalCPUListy == null){
            totalCPUListy = new ArrayList<Double>();
        }
        totalCPUListy.add(currentCPU);
        session.setAttribute("totalCPUListy",totalCPUListy);
        List<String> totalCPUListx = (ArrayList<String>)session.getAttribute("totalCPUListx");
        if(totalCPUListx == null){
            totalCPUListx = new ArrayList<String>();
        }
        totalCPUListx.add(MainUtils.getCurrentHMSTime());
        session.setAttribute("totalCPUListx",totalCPUListx);
        map.put("totalCPUListy",totalCPUListy);
        map.put("totalCPUListx",totalCPUListx);
//        关于memery
        List<Double> totalMEMListy = (ArrayList<Double>)session.getAttribute("totalMEMListy");
        if (totalMEMListy == null){
            totalMEMListy = new ArrayList<Double>();
        }
        totalMEMListy.add(currentMEM);
        session.setAttribute("totalMEMListy",totalMEMListy);
        List<String> totalMEMListx = (ArrayList<String>)session.getAttribute("totalMEMListx");
        if(totalMEMListx == null){
            totalMEMListx = new ArrayList<String>();
        }
        totalMEMListx.add(MainUtils.getCurrentHMSTime());
        session.setAttribute("totalMEMListx",totalMEMListx);
        map.put("totalMEMListy",totalMEMListy);
        map.put("totalMEMListx",totalMEMListx);
        return map;
    }

    @RequestMapping(value = "/nodeInfoUpdate")
    public @ResponseBody Map<Object,Object> NodeInfoUpdate(Model model,HttpSession session) throws ApiException {
        Map<Object,Object> map = new HashMap<Object, Object>();
        List<NodeInfo> nodeList = infoService.getNodeInfo();
        String masterName = null;
        for(NodeInfo nodeInfo : nodeList){
            if(nodeInfo.getRole().equalsIgnoreCase("master")){
                masterName = nodeInfo.getName();
            }
        }
//        model.addAttribute("nodeList",nodeList);
        String desiredName = (String)session.getAttribute("nodeName");
        if(desiredName == null){
            desiredName = masterName;
        }
        System.out.println("current desired node name: "+desiredName);
        session.setAttribute("nodeName",desiredName);
        NodeInfo currentNode = null;
        for(NodeInfo node : nodeList){
            if(node.getName().equals(desiredName)){
//                map.put("nodeInfo",node);
                currentNode = node;
//                map.put("time",MainUtils.getCurrentHMSTime());
                break;
            }
        }

//       图表1相关的内容
        List<String> chart1y = (List<String>) session.getAttribute("chart1y");
        if (chart1y == null){
            chart1y = new ArrayList<String>();
        }
        List<String> chart1x = (List<String>) session.getAttribute("chart1x");
        if(chart1x == null){
            chart1x = new ArrayList<String>();
        }
        assert currentNode != null;
        chart1y.add(String.valueOf(currentNode.getUsage().getCPUAmount()));
        chart1x.add(MainUtils.getCurrentHMSTime());
        session.setAttribute("chart1y",chart1y);
        session.setAttribute("chart1x",chart1x);
        map.put("chart1y",chart1y);
        map.put("chart1x",chart1x);

//        图表2相关内容
        List<String> chart2y = (List<String>) session.getAttribute("chart2y");
        if (chart2y == null){
            chart2y = new ArrayList<String>();
        }
        List<String> chart2x = (List<String>) session.getAttribute("chart2x");
        if(chart2x == null){
            chart2x = new ArrayList<String>();
        }
        chart2y.add(String.valueOf(currentNode.getUsage().getMemory()));
        chart2x.add(MainUtils.getCurrentHMSTime());
        session.setAttribute("chart2y",chart2y);
        session.setAttribute("chart2x",chart2x);
        map.put("chart2x",chart2x);
        map.put("chart2y",chart2y);

//        图表3相关内容：CPU的使用率
        List<String> chart3y = (List<String>) session.getAttribute("chart3y");
        if (chart3y == null){
            chart3y = new ArrayList<String>();
        }
        List<String> chart3x = (List<String>) session.getAttribute("chart3x");
        if(chart3x == null){
            chart3x = new ArrayList<String>();
        }
        chart3y.add(String.valueOf(currentNode.getUsage().getCPURatio()));
        chart3x.add(MainUtils.getCurrentHMSTime());
        session.setAttribute("chart3y",chart3y);
        session.setAttribute("chart3x",chart3x);
        map.put("chart3x",chart3x);
        map.put("chart3y",chart3y);

//        图表4相关内容：MEMERY利用率
        List<String> chart4y = (List<String>) session.getAttribute("chart4y");
        if (chart4y == null){
            chart4y = new ArrayList<String>();
        }
        List<String> chart4x = (List<String>) session.getAttribute("chart4x");
        if(chart4x == null){
            chart4x = new ArrayList<String>();
        }
        chart4y.add(String.valueOf(currentNode.getUsage().getMemoryRatio()));
        chart4x.add(MainUtils.getCurrentHMSTime());
        session.setAttribute("chart4y",chart4y);
        session.setAttribute("chart4x",chart4x);
        map.put("chart4x",chart4x);
        map.put("chart4y",chart4y);

//        图6相关内容：CPU需求量
//        double chart6 = currentNode.getUsage().get;


//      图8相关内容：DISK使用量
        List<String> chart8y = (List<String>) session.getAttribute("chart8y");
        if (chart8y == null){
            chart8y = new ArrayList<String>();
        }
        List<String> chart8x = (List<String>) session.getAttribute("chart8x");
        if(chart8x == null){
            chart8x = new ArrayList<String>();
        }
        chart8y.add(String.valueOf(currentNode.getUsage().getDisk()));
        chart8x.add(MainUtils.getCurrentHMSTime());
        session.setAttribute("chart8y",chart8y);
        session.setAttribute("chart8x",chart8x);
        map.put("chart8x",chart8x);
        map.put("chart8y",chart8y);

//        图9相关内容：DISK使用率
        List<String> chart9y = (List<String>) session.getAttribute("chart9y");
        if (chart9y == null){
            chart9y = new ArrayList<String>();
        }
        List<String> chart9x = (List<String>) session.getAttribute("chart9x");
        if(chart9x == null){
            chart9x = new ArrayList<String>();
        }
        chart9y.add(String.valueOf(currentNode.getUsage().getDiskRatio()));
        chart9x.add(MainUtils.getCurrentHMSTime());
        session.setAttribute("chart9y",chart9y);
        session.setAttribute("chart9x",chart9x);
        map.put("chart9x",chart9x);
        map.put("chart9y",chart9y);
        return map;
    }

    @RequestMapping(value = "/fun3")
    public @ResponseBody List<Double> function3(Model model){
        List<Double> ret = new ArrayList<Double>();
        a++;
        for (int i=0;i<10;i++){
            ret.add(a + i);
        }
        return ret;
    }
}
