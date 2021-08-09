package cn.edu.njust.service;

import cn.edu.njust.entity.*;
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
import java.util.ArrayList;
import java.util.List;

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
     * 根据pod名获取该pod的CPU、内存等信息<br>
     *     ！！！未完善！！
     * @param podName String pod名称
     * @return Usage 该pod的使用量信息
     */
    private Usage getUsageByPodName(String podName) {
        return null;
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

        V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);

        for (V1Pod item : list.getItems()) {
            String podName=item.getMetadata().getName();
            String podNamespace=item.getMetadata().getNamespace();
            String podStatus=item.getStatus().getPhase();
//            System.out.println("item.getStatus():"+item.getStatus());
//            String podAge=item.getStatus().getStartTime().toString();
            List<V1PodCondition> conditions = item.getStatus().getConditions();
            String podAge=conditions.get(conditions.size()-1).getLastTransitionTime().toString();
            List<V1ContainerStatus> containerStatuses = item.getStatus().getContainerStatuses();
            String containerName="null";
            if(containerStatuses!=null) containerName=containerStatuses.get(containerStatuses.size()-1).getName();
//            System.out.println("containerStatuses.get(last):"+containerStatuses);
            pod=new PodInfo(podNamespace,podName,podStatus,podAge,containerName);
            Usage usage=getUsageByPodName(podName);
            pod.setUsage(usage);
            res.add(pod);
        }
        return res;
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