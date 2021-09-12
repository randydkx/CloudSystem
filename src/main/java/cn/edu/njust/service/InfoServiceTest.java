package cn.edu.njust.service;

import cn.edu.njust.entity.ContainerInfo;
import cn.edu.njust.entity.Deployment;
import cn.edu.njust.entity.NodeInfo;
import cn.edu.njust.entity.PodInfo;
import cn.edu.njust.utils.MainUtils;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.ApiextensionsApi;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 测试类
 * 需要使用某个方法则把某个方法对应的@Test注释去掉
 * testGetContainerInfo还没写完
 */
public class InfoServiceTest {
        @Test
    public void testGetPodInfo() throws IOException, ApiException {
        InfoService infoService=new InfoService(MainUtils.KUBE_CONFIG_PATH);
        Map<Object,Object> result = infoService.getPodInfo(true);
        List<PodInfo> podInfos = (List<PodInfo>)result.get("totalPodList");
        List<PodInfo> forNode1 = (List<PodInfo>)result.get("forNode1");
        List<PodInfo> forNode2 = (List<PodInfo>)result.get("forNode2");
        List<PodInfo> forNode3 = (List<PodInfo>)result.get("forNode3");
        for(PodInfo podInfo:podInfos){
            System.out.println(podInfo);
        }
        System.out.println();
        for(PodInfo podInfo: forNode1){
            System.out.println(podInfo);
        }
        System.out.println();
        for(PodInfo podInfo: forNode2){
            System.out.println(podInfo);
        }
        System.out.println();
        for(PodInfo podInfo: forNode3){
            System.out.println(podInfo);
        }
    }

        @Test
    public void testGetNodeInfo() throws IOException, ApiException {
        InfoService infoService=new InfoService("C:\\config");
        List<NodeInfo> nodeInfos = infoService.getNodeInfo();
        for(NodeInfo nodeInfo:nodeInfos){
            System.out.println(nodeInfo);
        }
    }

    @Test
    public void testGetContainerInfo() throws IOException, ApiException {
        InfoService infoService=new InfoService("C:\\config");
        List<ContainerInfo> containerInfos = infoService.getContainerInfo();
        for(ContainerInfo containerInfo:containerInfos){
            System.out.println(containerInfo);
        }
    }

    @Test
    public void testGetDeploymentInfo() throws IOException, ApiException{
        InfoService infoService = new InfoService("C:\\config");
        List<Deployment> list = infoService.getAllDeploymentInfo();
        for (Deployment item:list){
            System.out.println(item);
        }
    }

    @Test
    public void testChangeReplicas()throws IOException, ApiException{
        InfoService infoService = new InfoService("C:\\config");
        infoService.changeDeploymentReplicas("default","rubis-deployment",1);
    }
}