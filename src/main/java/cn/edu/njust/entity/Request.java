package cn.edu.njust.entity;

import java.sql.Timestamp;

/**
 * @author TYX
 * @name Request
 * @description 请求的相关信息
 * @createTime 2021/6/25 13:33
 * 注意：kubernetes的request需要通过pod获取 也就是说基本上与pod有绑定关系
 **/
public class Request {
    private Timestamp arrivalTime;
    private String name;
    private String receiveAddr;
    //请求时间
    private String requestTime;

    /* 原项目代码的意思是 这是streamNamespacedPodLog返回的东西
     * AccessLog --- R-addr=192.168.83.5 --- Time=09/Apr/2020:02:09:01 +0000 --- Body_bytes_send=649
     * --- status=200 --- requesttime=0.068 --- upstreamaddr=10.244.0.94:80 ---upstreamlength=649
     * --- upstreamresponsetime=0.068 --- httpreferer=http://myrubis.czc/PHP/register.html
     * @param log
     */

    /* 原项目代码处理结束后：
I0623 12:53:33.168505       7 event.go:281] Event(v1.ObjectReference{Kind:"Ingress", Namespace:"default", Name:"rubis-ingress", UID:"213304b3-f8a8-4478-bb72-b4b794a74878", APIVersion:"networking.k8s.io/v1beta1", ResourceVersion:"210290", FieldPath:""}): type: 'Normal' reason: 'UPDATE' Ingress default/rubis-ingress
AccessLog --- R-addr=192.168.7.10 --- Time=23/Jun/2021:12:54:09 +0000 --- Body_bytes_send=8 --- status=200 --- requesttime=0.025 --- upstreamaddr=10.122.1.8:8080 ---upstreamlength=8 --- upstreamresponsetime=0.025 --- reqid=2ce670c57d856e2a11881a4de1b16110
Set controllercenter.readingoldlog=false
     */
}
