package cn.edu.njust.controller;

import cn.edu.njust.entity.NodeInfo;
import cn.edu.njust.service.InfoService;
import cn.edu.njust.utils.MainUtils;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 视图控制器，用于控制视图之间的跳转
 */
@Controller
@RequestMapping(value = "/to")
public class ViewController {
    InfoService infoService = null;

    ViewController() throws IOException {
        infoService = new InfoService(MainUtils.KUBE_CONFIG_PATH);
    }
    /**
     * 定向到index.jsp
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String toIndexPage(Model model) throws ApiException{
        List<NodeInfo> nodeList = infoService.getNodeInfo();
        model.addAttribute("nodeList",nodeList);
        model.addAttribute("defaultMaster",true);
        return "page/index";
    }

    @RequestMapping("/deployment")
    public String toDeploymentPage(Model model){
        return "page/deployment";
    }

    @RequestMapping("/load")
    public String toLoadPage(Model model){
        return "page/load";
    }

    /**
     * 定向到nodes.jsp，获取node的所有信息
     * @param model
     * @return
     */
    @RequestMapping("/nodes")
    public String toNodesPage(Model model, HttpSession session) throws ApiException {
        List<NodeInfo> nodeList = infoService.getNodeInfo();
        String masterName = null;
        for(NodeInfo nodeInfo : nodeList){
            if(nodeInfo.getRole().equalsIgnoreCase("master")){
                masterName = nodeInfo.getName();
            }
        }
        model.addAttribute("nodeList",nodeList);
        String desiredName = (String)session.getAttribute("nodeName");
        if(desiredName == null){
            desiredName = masterName;
        }
        session.setAttribute("nodeName",desiredName);
        for(NodeInfo node : nodeList){
            if(node.getName().equals(desiredName)){
                model.addAttribute("nodeInfo",node);
                model.addAttribute("time",MainUtils.getCurrentHMSTime());
                break;
            }
        }
        return "page/nodes";
    }
}
