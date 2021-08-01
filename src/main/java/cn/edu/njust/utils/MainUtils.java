package cn.edu.njust.utils;

import cn.edu.njust.entity.PodInfo;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.BoundType;
import org.apache.commons.io.filefilter.FalseFileFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainUtils {

    public static void main(String[] args) {
        PodInfo podInfo = new PodInfo("1231","fadf","afasd","fadfs","fadf");
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<PodInfo> list = new ArrayList<PodInfo>();
        list.add(podInfo);
        map.put("PodList",list);
        String jsonstring = JSON.toJSONString(map,false);
        System.out.println(jsonstring);
        System.out.println(podInfo);
        Object obj = JSON.parse(jsonstring);
//        System.out.println(map2.get("PodList"));
    }
}
