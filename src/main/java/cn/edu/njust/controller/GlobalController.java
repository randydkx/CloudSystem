package cn.edu.njust.controller;

import cn.edu.njust.entity.*;
import cn.edu.njust.entity.Deployment;
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
    public InfoService infoService = null;

    GlobalController() throws IOException {infoService = new InfoService(MainUtils.KUBE_CONFIG_PATH);}

    /**
     * 关于rubis-deployment的请求列表信息Ajax接口
     * @param model
     * @return
     */
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
        double minRandom = - 1;
        double maxRandom = + 1;
        currentCPU += Math.random() * (maxRandom - minRandom) * 2 + minRandom * 2;
        currentMEM += Math.random() * (maxRandom - minRandom) * 2 + minRandom * 2;
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

    /**
     * node.jsp页面通过Ajax刷新的请求
     * @param model
     * @param session
     * @return
     * @throws ApiException
     */
    @RequestMapping(value = "/nodeInfoUpdate")
    public @ResponseBody Map<Object,Object> NodeInfoUpdate(Model model,HttpSession session) throws ApiException {
        List<NodeInfo> nodeList = infoService.getNodeInfo();

        Integer desiredIndex = (Integer) session.getAttribute("nodeIndex");
        assert desiredIndex != null;
        String desiredName = nodeList.get(desiredIndex).getName();

        System.out.println("current desired node name  =====>"+ desiredName);
        session.setAttribute("nodeIndex",desiredIndex);

//        根据session中的对应节点的对象处理该节点的数据
        Map<Object,Object> node = (Map<Object, Object>)session.getAttribute(desiredName);
        if (node == null)node=new HashMap<Object, Object>();
        NodeInfo currentNode = null;
        for(NodeInfo nodeInfo : nodeList){
            if(nodeInfo.getName().equals(desiredName)){
                currentNode = nodeInfo;
                break;
            }
        }

//       图表1相关的内容
        List<String> chart1y = (List<String>)node.get("chart1y");
        if (chart1y == null){
            chart1y = new ArrayList<String>();
        }
        List<String> chart1x = (List<String>) node.get("chart1x");
        if(chart1x == null){
            chart1x = new ArrayList<String>();
        }
        assert currentNode != null;
        double minRandom = - 5;
        double maxRandom = + 5;
        double cpuRandom = Math.random() * (maxRandom - minRandom) * 4 + minRandom * 4;
        double memRandom = Math.random() * (maxRandom - minRandom) * 2 + minRandom * 2;
        double diskRandom = Math.random() * (maxRandom - minRandom) * 1 +minRandom * 1;
        chart1y.add(String.valueOf(currentNode.getUsage().getCPUAmount() + cpuRandom));
        chart1x.add(MainUtils.getCurrentHMSTime());
        node.put("chart1y",chart1y);
        node.put("chart1x",chart1x);
        session.setAttribute("chart1y",chart1y);
        session.setAttribute("chart1x",chart1x);

//        图表2相关内容
        List<String> chart2y = (List<String>) node.get("chart2y");
        if (chart2y == null){
            chart2y = new ArrayList<String>();
        }
        List<String> chart2x = (List<String>) node.get("chart2x");
        if(chart2x == null){
            chart2x = new ArrayList<String>();
        }
        chart2y.add(String.valueOf(currentNode.getUsage().getMemory() + memRandom));
        chart2x.add(MainUtils.getCurrentHMSTime());
        node.put("chart2y",chart2y);
        node.put("chart2x",chart2x);

//        图表3相关内容：CPU的使用率
        List<String> chart3y = (List<String>) node.get("chart3y");
        if (chart3y == null){
            chart3y = new ArrayList<String>();
        }
        List<String> chart3x = (List<String>) node.get("chart3x");
        if(chart3x == null){
            chart3x = new ArrayList<String>();
        }
        chart3y.add(String.valueOf(currentNode.getUsage().getCPURatio() + cpuRandom / 20));
        chart3x.add(MainUtils.getCurrentHMSTime());
        node.put("chart3y",chart3y);
        node.put("chart3x",chart3x);

//        图表4相关内容：MEMERY利用率
        List<String> chart4y = (List<String>) node.get("chart4y");
        if (chart4y == null){
            chart4y = new ArrayList<String>();
        }
        List<String> chart4x = (List<String>) node.get("chart4x");
        if(chart4x == null){
            chart4x = new ArrayList<String>();
        }
        chart4y.add(String.valueOf(currentNode.getUsage().getMemoryRatio() + memRandom / (currentNode.getUsage().getMemoryRatio()>20?5:20)));
        chart4x.add(MainUtils.getCurrentHMSTime());
        node.put("chart4y",chart4y);
        node.put("chart4x",chart4x);

//        图6相关内容：CPU需求量
//        double chart6 = currentNode.getUsage().get;


//      图8相关内容：DISK使用量
        List<String> chart8y = (List<String>) node.get("chart8y");
        if (chart8y == null){
            chart8y = new ArrayList<String>();
        }
        List<String> chart8x = (List<String>) node.get("chart8x");
        if(chart8x == null){
            chart8x = new ArrayList<String>();
        }
        chart8y.add(String.valueOf(currentNode.getUsage().getDisk() + diskRandom));
        chart8x.add(MainUtils.getCurrentHMSTime());
        node.put("chart8y",chart8y);
        node.put("chart8x",chart8x);

//        图9相关内容：DISK使用率
        List<String> chart9y = (List<String>) node.get("chart9y");
        if (chart9y == null){
            chart9y = new ArrayList<String>();
        }
        List<String> chart9x = (List<String>) node.get("chart9x");
        if(chart9x == null){
            chart9x = new ArrayList<String>();
        }
        chart9y.add(String.valueOf(currentNode.getUsage().getDiskRatio() + diskRandom / 500));
        chart9x.add(MainUtils.getCurrentHMSTime());
        node.put("chart9y",chart9y);
        node.put("chart9x",chart9x);

//        将对应于某节点的信息存到session中，保持本次观察时间内可重复取用
        session.setAttribute(desiredName,node);
        return node;
    }

    /**
     * 获取node页面上的所有信息
     * @param model
     * @param session
     * @return
     * @throws ApiException
     */
    @RequestMapping(value = "/getPodInfoForAllNodes")
    public @ResponseBody Map<Object,Object> getPodInfoForAllNodes(Model model,HttpSession session)throws ApiException{
        Map<Object,Object> result = infoService.getPodInfo(true);
//        List<PodInfo> podInfos = (List<PodInfo>)result.get("totalPodList");
        List<PodInfo> forNode1 = (List<PodInfo>)result.get("forNode1");
        List<PodInfo> forNode2 = (List<PodInfo>)result.get("forNode2");
        List<PodInfo> forNode3 = (List<PodInfo>)result.get("forNode3");
        PodInfo rubis_deployment = null;
//        查找rubis-deployment
        for(PodInfo podInfo : forNode2){
            if(podInfo.getName().contains(MainUtils.DEPLOYMENT_NAME)){
                rubis_deployment = podInfo;
                break;
            }
        }
        assert rubis_deployment != null;
        Map<Object,Object> podPage = null;
        if(session.getAttribute("podPage") == null){
            podPage = new HashMap<Object, Object>();
        }else{
            podPage = (Map<Object,Object>)session.getAttribute("podPage");
        }
        //        根据session取该应用的资源用量
        List<Double> cpuUsageList = null;
        List<Double> memUsageList = null;
        List<String> timeLine = null;
        assert podPage!=null;
        if(podPage.containsKey("cpuUsageList") == false){
            assert podPage.containsKey("memUsageList") == false;
            assert podPage.containsKey("timeLine") == false;
            cpuUsageList = new ArrayList<Double>();
            memUsageList = new ArrayList<Double>();
            timeLine = new ArrayList<String>();
        }else{
            cpuUsageList = (List<Double>)podPage.get("cpuUsageList");
            memUsageList = (List<Double>)podPage.get("memUsageList");
            timeLine = (List<String>)podPage.get("timeLine");
        }

        cpuUsageList.add(rubis_deployment.getUsage().getCPURatio());
        memUsageList.add(rubis_deployment.getUsage().getMemoryRatio());
        timeLine.add(MainUtils.getCurrentHMSTime());
        podPage.put("cpuUsageList",cpuUsageList);
        podPage.put("memUsageList",memUsageList);
        podPage.put("timeLine",timeLine);
        podPage.put("forNode1",forNode1);
        podPage.put("forNode2",forNode2);
        podPage.put("forNode3",forNode3);
        session.setAttribute("podPage",podPage);
        return podPage;
    }


    @RequestMapping(value = "/increase")
    public String increase(@RequestParam String name,@RequestParam int num,@RequestParam String namespace){
//        String name = "rubis-deployment";
        System.out.println(name+" "+num+" "+namespace);
        infoService.changeDeploymentReplicas(namespace,name,num + 1);
        return "redirect: toload.do";
    }

    @RequestMapping(value = "/decrease")
    public String decrease(@RequestParam String name,@RequestParam int num,@RequestParam String namespace){
//        String name = "rubis-deployment";
        if(num > 1){
            infoService.changeDeploymentReplicas(namespace,name,num - 1);
        }
        return "redirect: toload.do";
    }

    @RequestMapping(value = "/toload")
    public String toload(){
        return "page/load";
    }

    /**
     * Ajax请求获取所有deployment
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/getDeploymentList")
    public @ResponseBody List<Deployment> getDeployment(Model model,HttpSession session){
        return infoService.getAllDeploymentInfo();
    }

}
