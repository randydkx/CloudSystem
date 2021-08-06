package cn.edu.njust.service;

import cn.edu.njust.entity.NodeInfo;
import cn.edu.njust.entity.PodInfo;
import io.kubernetes.client.openapi.ApiException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 测试类
 * 需要使用某个方法则把某个方法对应的@Test注释去掉
 * testGetContainerInfo还没写完
 */
public class InfoServiceTest {
    //    @Test
    public void testGetPodInfo() throws IOException, ApiException {
        InfoService infoService=new InfoService("C:\\config");
        List<PodInfo> podInfos = infoService.getPodInfo();
        for(PodInfo podInfo:podInfos){
            System.out.println(podInfo);
        }
    }

    //    @Test
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
        infoService.getContainerInfo();
    }
}