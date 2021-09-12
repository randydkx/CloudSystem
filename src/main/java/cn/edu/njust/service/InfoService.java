package cn.edu.njust.service;

import cn.edu.njust.entity.*;
import cn.edu.njust.utils.linuxshell.LinuxDataBase;
import cn.edu.njust.utils.linuxshell.LinuxShellUtil;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.ExtensionsV1beta1Api;
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
import java.util.Map;

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
    private AppsV1Api v1api;

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
        v1api = new AppsV1Api();
    }

    /**
     * 根据podName+Node的pod信息导出Usage
     * @param podName
     * @param result
     * @return
     */
    public static HashMap<Object,Object> getUsageByPodName(String podName, String result){
//        if(result == null || result.length() == 0)return null;

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
     * 大致思路是根据空格和换行符分割文本<br>
     * 由于COMMAND一般有引号修饰，所以用引号作为每一行的标识
     * @return List<ContainerInfo> 容器相关数据的list
     * @throws ApiException api调用异常
     */
    public List<ContainerInfo> getContainerInfo() throws ApiException {
        List<ContainerInfo> res=new ArrayList<>();
        ContainerInfo containerInfo;
        LinuxDataBase masterDataBase=new LinuxDataBase(MainUtils.USERNAME,MainUtils.PASSWORD,
                MainUtils.MASTER_IP, MainUtils.PORT);
//        LinuxDataBase dataBase=new LinuxDataBase("root","991011","192.168.52.10","22");
        LinuxShellUtil linux = new LinuxShellUtil();
        HashMap shellHashMap = linux.getData(masterDataBase, "docker ps -a");
        String shellString= (String) shellHashMap.get("return");

        String[] s = shellString.split("\\s{2,}|\\n");
        int i=0;
        for(String str:s){
//            System.out.println(i+":"+str);
            if(str.contains("\"")){
                String containerName=s[i+3];
                String image=s[i-1];
                containerInfo=new ContainerInfo(containerName,image);
                res.add(containerInfo);
            }
            i++;
        }
        return res;
    }

    /**
     * getPodInfo 获取pod相关信息
     * @return List<PodInfo> pod相关数据的list
     * @throws ApiException api调用异常
     */
    public Map<Object,Object> getPodInfo(Boolean CONTAIN_3_NODES) throws ApiException {
        Map<Object,Object> res = new HashMap<Object, Object>();
        List<PodInfo> totalPodList = new ArrayList<>();
        PodInfo pod;
//        获取node所有pod相关的信息
        LinuxDataBase masterDataBase=new LinuxDataBase(MainUtils.USERNAME,MainUtils.PASSWORD,
                MainUtils.MASTER_IP, MainUtils.PORT);

        LinuxShellUtil linux = new LinuxShellUtil();
//        通过listpod获取的是集群的全部pod
        String result1 = (String) linux.getData(masterDataBase,"kubectl describe node " + MainUtils.MASTER_NAME).get("return");
        String result2 = (String) linux.getData(masterDataBase,"kubectl describe node " + MainUtils.NODE_ONE_NAME).get("return");
//        获取node-2的pod信息，当前未开node-2
        String result3 = null;
        if (CONTAIN_3_NODES){
            result3 = (String) linux.getData(masterDataBase,"kubectl describe node " + MainUtils.NODE_TWO_NAME).get("return");
        }

        V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);

        List<PodInfo> forNode1 = new ArrayList<>();
        List<PodInfo> forNode2 = new ArrayList<>();
        List<PodInfo> forNode3 = new ArrayList<>();

        for (V1Pod item : list.getItems()) {
            String podName=item.getMetadata().getName();
            String podNamespace=item.getMetadata().getNamespace();
            String podStatus=item.getStatus().getPhase();

            List<V1PodCondition> conditions = item.getStatus().getConditions();
            if(conditions == null)continue;
            String podAge=conditions.get(0).getLastTransitionTime().toString();
//            String podAge=conditions.get(conditions.size()-1).getLastTransitionTime().toString();
            List<V1ContainerStatus> containerStatuses = item.getStatus().getContainerStatuses();
            String containerName="null";
            if(containerStatuses!=null)
                containerName=containerStatuses.get(containerStatuses.size()-1).getName();

            pod=new PodInfo(podNamespace,podName,podStatus,podAge,containerName);
            HashMap<Object,Object> map  = getUsageByPodName(podName,result1);
            if((Boolean) map.get("hasPod")){
                pod.setUsage((Usage)map.get("usage"));
                pod.setAge((String)map.get("age"));
                forNode1.add(pod);
            }else{
                map = getUsageByPodName(podName,result2);
                if((Boolean)map.get("hasPod")){
                    pod.setUsage((Usage)map.get("usage"));
                    pod.setAge((String)map.get("age"));
                    forNode2.add(pod);
                }else{
//                    包含第三个节点的时候对第三个节点中的pod的Usage进行获取
                    if(CONTAIN_3_NODES){
                        map = getUsageByPodName(podName,result3);
                        if((Boolean)map.get("hasPod")){
                            pod.setUsage((Usage)map.get("usage"));
                            pod.setAge((String)map.get("age"));
                            forNode3.add(pod);
                        }
                    }
                }
            }

            totalPodList.add(pod);

        }
        res.put("totalPodList",totalPodList);
        res.put("forNode1",forNode1);
        res.put("forNode2",forNode2);
        res.put("forNode3",forNode3);
        return res;
    }
    /**
     * 获取所有命名空间的deployment
     * @return
     */
    public List<Deployment> getAllDeploymentInfo() {
        List<Deployment> ret = new ArrayList<>();
        try {
            V1DeploymentList result = v1api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null,null);
            for(V1Deployment item : result.getItems()){
//                System.out.println(result);
                Deployment deployment = new Deployment();
                deployment.setName(item.getMetadata().getName());
                int available = (item.getStatus().getAvailableReplicas() == null) ? 0 : item.getStatus().getAvailableReplicas();
                int total = item.getStatus().getReplicas();
                deployment.setReady(available + "/" + total);
                deployment.setAvailable(available);
                deployment.setUpToDate(item.getStatus().getUpdatedReplicas());
                deployment.setNamespace(item.getMetadata().getNamespace());
                ret.add(deployment);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 更新副本数量
     * @param namespace
     * @param name
     * @param replicas
     * @return
     */
    public boolean changeDeploymentReplicas(String namespace, String name, int replicas) {
        if (replicas <= 0 ) return false;
        String jsonPatchStr = "[{\"op\":\"replace\",\"path\":\"/spec/replicas\", \"value\": " + replicas + " }]";
        V1Patch body = new V1Patch(jsonPatchStr);
        try {
            V1Deployment result1 = v1api.patchNamespacedDeployment(name, namespace, body, null, null, null, null);
        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println("replicas 更新失败！");
            return false;
        }

        return true;
    }

    public HashMap<Object,Object> getNodeUsageByName(String nodeName){
        LinuxDataBase masterDataBase=new LinuxDataBase(MainUtils.USERNAME,MainUtils.PASSWORD,
                MainUtils.MASTER_IP, MainUtils.PORT);

        LinuxShellUtil linux = new LinuxShellUtil();
        HashMap<Object,Object> map = new HashMap<Object, Object>();
//        通过listpod获取的是集群的全部pod
        String result = null;
        int count = 0;
        while(result == null){
            result = (String) linux.getData(masterDataBase,"kubectl describe node " + nodeName).get("return");
            count ++;
        }
        System.out.println("getData次数："+count);
//        System.out.println(result);
        Usage usage = new Usage();
        assert result != null ;
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
            if (items[0].equals("Capacity:")){
                String[] diskCapacityLine = resultLine[i + 2].trim().split("\\s+");
//                结果使用Ki表示的
                if(diskCapacityLine[1].contains("Ki")){
                    usage.setDisk(MainUtils.limitPrecision((Double.parseDouble(diskCapacityLine[1].substring(0,diskCapacityLine[1].length() - 2)) / (1024 * 1024)),2));
                }else {
                    usage.setDisk(MainUtils.limitPrecision((Double.parseDouble(diskCapacityLine[1]) / (1024 * 1024 * 1024)),2));
                }
            }
            if (items[0].equals("Allocatable:")){
                String[] diskCapacityLine = resultLine[i + 2].trim().split("\\s+");
                Double total = usage.getDisk();
                Double unused = 0.0;
//                不同的表示按照不同的计算法得到GB形式的数据

                if(diskCapacityLine[1].contains("Ki")){
                    unused = Double.parseDouble(diskCapacityLine[1].substring(0,diskCapacityLine[1].length() - 2)) / (1024 * 1024);
                }else{
                    unused = Double.parseDouble(diskCapacityLine[1]) / (1024 * 1024 * 1024);
                }

                usage.setDiskRatio(MainUtils.limitPrecision((total - unused) / total,2));
            }
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
            if(!map.containsKey("usage") || !map.containsKey("role") || !map.containsKey("coreNum")){
                nodeInfo.setUsage(new Usage());
                nodeInfo.setCoreNum(0);
                nodeInfo.setRole("node");
                continue;
            }

            nodeInfo.setUsage((Usage)map.get("usage"));
            nodeInfo.setCoreNum((int)map.get("coreNum"));
            nodeInfo.setRole((String)map.get("role"));
            res.add(nodeInfo);
        }

        return res;
    }

}