package cn.edu.njust.utils;

import cn.edu.njust.entity.PodInfo;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.BoundType;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.io.filefilter.FalseFileFilter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainUtils {

/** tyx **/

//    public static String MASTER_IP = "192.168.52.10";
//    public static String USERNAME = "test";
//    public static String PASSWORD = "991011";

/** lws **/
    public static String USERNAME = "root";
    public static String PASSWORD = "LWS17851093886.";
    public static String MASTER_IP = "192.168.7.10";
    public static String NODE_ONE_IP = "192.168.7.20";
    public static String NODE_TWO_IP = "192.168.7.30";
    public static String MASTER_NAME = "kube-1";
    public static String NODE_ONE_NAME = "node-1";
    public static String NODE_TWO_NAME = "node-2";
    public static String KUBE_CONFIG_PATH = "C:\\config";
    //是否包含第三个节点
    public static Boolean CONTAIN_3_NODES = false;
    //用于pod资源专门可视化的pod名称
    public static String DEPLOYMENT_NAME = "rubis-deployment";


    public static String PORT = "22";

//    限制double数据小数点后尾位数，四舍五入法则
    public static double limitPrecision(Double value,int precision){
        return  new BigDecimal(value).setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String getCurrentHMSTime(){
        Calendar cal = Calendar.getInstance();
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public static void main(String[] args) {
//        PodInfo podInfo = new PodInfo("1231","fadf","afasd","fadfs","fadf");
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        List<PodInfo> list = new ArrayList<PodInfo>();
//        list.add(podInfo);
//        map.put("PodList",list);
//        String jsonstring = JSON.toJSONString(map,false);
//        System.out.println(jsonstring);
//        System.out.println(podInfo);
//        Object obj = JSON.parse(jsonstring);
////        System.out.println(map2.get("PodList"));
        Map<Integer,Double> map = new HashMap<>();
        System.out.println(map.get(1));
    }
}
