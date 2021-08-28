package cn.edu.njust.service;

import cn.edu.njust.entity.*;
import cn.edu.njust.utils.linuxshell.LinuxDataBase;
import cn.edu.njust.utils.linuxshell.LinuxShellUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.edu.njust.utils.*;

import javax.activation.MailcapCommandMap;

/**
 * @author TYX
 * @name InfoService
 * @description 获取当前的资源使用量以及有关集群容器、Pod、Node的信息
 * @createTime 2021/7/15 21:01
 **/
public class InfoService {
    private CoreV1Api api;

    /**
     * Constructor<br>
     *     构造时根据Kubernetes配置文件路径初始化，并初始化了api
     * @param kubeConfigPath Kubernetes配置文件路径
     * @throws IOException 文件读取异常
     */
    public InfoService(String kubeConfigPath) throws IOException {
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        Configuration.setDefaultApiClient(client);
        api = new CoreV1Api();
    }

    /**
     * 根据podName+Node的pod信息导出Usage
     * @param podName
     * @param result
     * @return
     */
    public static HashMap<Object,Object> getUsageByPodName(String podName, String result){

        String[] resultLine = result.split("\n");
        Usage usage = new Usage();
        HashMap<Object,Object> map = new HashMap<>();
        boolean hasPod = false;
        for(int i=0;i<resultLine.length;i++){
            String line = resultLine[i];
            String[] items = line.trim().split("\\s+");
//            对pod的用量信息进行搜集
            if( items[0].equals("Non-terminated") ){
                int num = Integer.parseInt(items[2].substring(1));
                for(int j=i+3;j<=i+num+2;j++){
                    String[] podLine = resultLine[j].trim().split("\\s+");
                    if (podLine[1].equals(podName)){
                        hasPod = true;
                        if(podLine[2].equals("0")){
                            usage.setCPUAmount(0);
                            usage.setCPUAmountStr("0m");
                        }else{
                            usage.setCPUAmount(Double.parseDouble(podLine[2].substring(0,podLine[2].length() - 1)));
                            usage.setCPUAmountStr(podLine[2]);
                        }
                        usage.setCPURatio(Double.parseDouble(podLine[3].substring(1,podLine[3].length() - 2)));

//                        特殊处理0的情况
                        if(podLine[6].equals("0")){
                            usage.setMemory(0);
                            usage.setMemoryStr("0Mi");
                        }else{
                            usage.setMemory(Double.parseDouble(podLine[6].substring(0,podLine[6].length() - 2)));
                            usage.setMemoryStr(podLine[6]);
                        }

                        usage.setMemoryRatio(Double.parseDouble(podLine[7].substring(1,podLine[7].length() - 2)));
                        map.put("usage",usage);
                        map.put("age",podLine[podLine.length- 1]);
                    }
                }
            }
        }
        map.put("hasPod",hasPod);

        return map;
    }

    /**
     * getContainerInfo 返回容器相关数据<br>
     * ！！！！没写完！！！！
     * @return List<ContainerInfo> 容器相关数据的list
     * @throws ApiException api调用异常
     */
    public List<ContainerInfo> getContainerInfo() throws ApiException {
        List<ContainerInfo> res=new ArrayList<>();
        ContainerInfo containerInfo;

        V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);

        for (V1Pod item : list.getItems()) {

            System.out.println(item.getMetadata());
            System.out.println(item.getStatus());
        }
        return res;
    }

    /**
     * getPodInfo 获取pod相关信息
     * @return List<PodInfo> pod相关数据的list
     * @throws ApiException api调用异常
     */
    public List<PodInfo> getPodInfo() throws ApiException {
        List<PodInfo> res=new ArrayList<PodInfo>();
        PodInfo pod;
//        获取node所有pod相关的信息
        LinuxDataBase masterDataBase=new LinuxDataBase(MainUtils.USERNAME,MainUtils.PASSWORD,
                MainUtils.MASTER_IP, MainUtils.PORT);

        LinuxShellUtil linux = new LinuxShellUtil();
//        通过listpod获取的是集群的全部pod
        String result1 = (String) linux.getData(masterDataBase,"kubectl describe node " + MainUtils.MASTER_NAME).get("return");
        String result2 = (String) linux.getData(masterDataBase,"kubectl describe node " + MainUtils.NODE_ONE_NAME).get("return");
//        获取node-2的pod信息，当前未开node-2
//        String result3 = (String) linux.getData(masterDataBase,"kubectl describe node " + MainUtils.NODE_TWO_NAME).get("return");

        V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);

        for (V1Pod item : list.getItems()) {
            String podName=item.getMetadata().getName();
            String podNamespace=item.getMetadata().getNamespace();
            String podStatus=item.getStatus().getPhase();

            List<V1PodCondition> conditions = item.getStatus().getConditions();
            String podAge=conditions.get(conditions.size()-1).getLastTransitionTime().toString();
            List<V1ContainerStatus> containerStatuses = item.getStatus().getContainerStatuses();
            String containerName="null";
            if(containerStatuses!=null)
                containerName=containerStatuses.get(containerStatuses.size()-1).getName();

            pod=new PodInfo(podNamespace,podName,podStatus,podAge,containerName);
            HashMap<Object,Object> map  = getUsageByPodName(podName,result1);
            if((Boolean) map.get("hasPod")){
                pod.setUsage((Usage)map.get("usage"));
                pod.setAge((String)map.get("age"));
            }else{
                map = getUsageByPodName(podName,result2);
                if((Boolean)map.get("hasPod")){
                    pod.setUsage((Usage)map.get("usage"));
                    pod.setAge((String)map.get("age"));
                }else{
//                    map = getUsageByPodName(podName,result3);
//                    if((Boolean)map.get("hasPod")){
//                        System.out.println("node2");
//                        pod.setUsage((Usage)map.get("usage"));
//                        pod.setAge((String)map.get("age"));
//                    }
                }
            }

            res.add(pod);
        }
        return res;
    }

    public HashMap<Object,Object> getNodeUsageByName(String nodeName){
        LinuxDataBase masterDataBase=new LinuxDataBase(MainUtils.USERNAME,MainUtils.PASSWORD,
                MainUtils.MASTER_IP, MainUtils.PORT);

        LinuxShellUtil linux = new LinuxShellUtil();
        HashMap<Object,Object> map = new HashMap<Object, Object>();
//        通过listpod获取的是集群的全部pod
        String result = (String) linux.getData(masterDataBase,"kubectl describe node " + nodeName).get("return");
//        System.out.println(result);
        Usage usage = new Usage();
        String[] resultLine = result.split("\n");
        for(int i=0;i<resultLine.length;i++){
            String[] items = resultLine[i].trim().split("\\s+");
            if((items.length == 3) && items[0].equals("Resource") && items[1].equals("Requests") && items[2].equals("Limits")){
                int cpuIndex = i + 2;
                int memoryIndex = i + 3;
                String[] cpuLine = resultLine[cpuIndex].trim().split("\\s+");
                String[] memoryLine = resultLine[memoryIndex].trim().split("\\s+");
                if (cpuLine[1].equals("0")){
                    usage.setCPUAmount(0);
                    usage.setCPUAmountStr("0m");
                }else{
                    usage.setCPUAmount(Double.parseDouble(cpuLine[1].substring(0,cpuLine[1].length() - 1)));
                    usage.setCPUAmountStr(cpuLine[1]);
                }
                usage.setCPURatio(Double.parseDouble(cpuLine[2].substring(1,cpuLine[2].length() - 2)));
                if(memoryLine[1].equals("0")){
                    usage.setMemoryStr("0Mi");
                    usage.setMemory(0);
                }else{
                    usage.setMemoryStr(memoryLine[1]);
                    usage.setMemory(Double.parseDouble(memoryLine[1].substring(0,cpuLine[1].length() - 2)));
                }
                usage.setMemoryRatio(Double.parseDouble(memoryLine[2].substring(1,memoryLine[2].length() - 2)));
            }
            if(items[0].equals("Roles:")){
                map.put("role",items[1].equals("<none>") ? "node":"master");
            }
            if (items[0].equals("cpu:")){
                map.put("coreNum",Integer.parseInt(items[1]));
            }
//            计算磁盘总量以及磁盘已用(得到的是瞬时存储)
//            if (items[0].equals("Capacity:")){
//                String[] diskCapacityLine = resultLine[i + 2].trim().split("\\s+");
////                结果使用Ki表示的
//                if(diskCapacityLine[1].contains("Ki")){
//                    usage.setDisk(MainUtils.limitPrecision((Double.parseDouble(diskCapacityLine[1].substring(0,diskCapacityLine[1].length() - 2)) / (1024 * 1024)),2));
//                }else {
//                    usage.setDisk(MainUtils.limitPrecision((Double.parseDouble(diskCapacityLine[1]) / (1024 * 1024 * 1024)),2));
//                }
//            }
//            if (items[0].equals("Allocatable:")){
//                String[] diskCapacityLine = resultLine[i + 2].trim().split("\\s+");
//                Double total = usage.getDisk();
//                Double unused = 0.0;
////                不同的表示按照不同的计算法得到GB形式的数据
//
//                if(diskCapacityLine[1].contains("Ki")){
//                    unused = Double.parseDouble(diskCapacityLine[1].substring(0,diskCapacityLine[1].length() - 2)) / (1024 * 1024);
//                }else{
//                    unused = Double.parseDouble(diskCapacityLine[1]) / (1024 * 1024 * 1024);
//                }
//                System.out.println(unused);
//
//                usage.setDiskRatio(MainUtils.limitPrecision((total - unused) / total,2));
//            }
        }
        map.put("usage",usage);
        return map;
    }

    /**
     * getNodeInfo 返回node相关信息
     * @return List<NodeInfo> node相关信息的list
     * @throws ApiException api调用异常
     */
    public List<NodeInfo> getNodeInfo() throws ApiException {
        List<NodeInfo> res=new ArrayList<>();
        NodeInfo nodeInfo;

        V1NodeList list=api.listNode(null,null,null,null,null,null,null,null,null);
        for (V1Node item : list.getItems()) {
            List<V1NodeAddress> addresses = item.getStatus().getAddresses();
            String nodeName=addresses.get(1).getAddress();
            String nodeAddress=addresses.get(0).getAddress();
            nodeInfo=new NodeInfo(nodeAddress,nodeName);
//            通过nodeName获取节点的CPU和内存用量
            HashMap<Object,Object> map = this.getNodeUsageByName(nodeInfo.getName());
            nodeInfo.setUsage((Usage)map.get("usage"));
            nodeInfo.setCoreNum((int)map.get("coreNum"));
            nodeInfo.setRole((String)map.get("role"));
            res.add(nodeInfo);
        }

        return res;
    }

//    public static void main(String[] args) throws IOException, ApiException {
//        InfoService infoService=new InfoService();
//        List<PodInfo> podInfo = infoService.getPodInfo("C:\\config");
//        for(PodInfo pod:podInfo){
//            System.out.println(pod);
//        }
//    }
}