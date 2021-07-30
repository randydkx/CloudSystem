package cn.edu.njust.service;

import cn.edu.njust.entity.*;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ContainerStatus;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodCondition;
import io.kubernetes.client.openapi.models.V1PodList;
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
    public Usage getUsage() {
        return null;
    }

    public List<ContainerInfo> getContainerInfo() {
        return null;
    }

    public List<PodInfo> getPodInfo(String kubeConfigPath) throws IOException, ApiException {
        ApiClient client=null;
        List<PodInfo> res=new ArrayList<PodInfo>();
        PodInfo pod;
//        String kubeConfigPath = "C:\\config";   //指定config
        client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
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
            res.add(pod);
        }
        return res;
    }

    public List<NodeInfo> getNodeInfo() {
        return null;
    }

    public static void main(String[] args) throws IOException, ApiException {
        InfoService infoService=new InfoService();
        List<PodInfo> podInfo = infoService.getPodInfo("C:\\config");
        for(PodInfo pod:podInfo){
            System.out.println(pod);
        }
    }
}
