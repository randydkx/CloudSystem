package cn.edu.njust.controller;

import cn.edu.njust.entity.NodeInfo;
import cn.edu.njust.service.InfoService;
import cn.edu.njust.utils.MainUtils;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.faces.component.html.HtmlPanelGrid;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
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
    public String toIndexPage(Model model, HttpSession session) throws ApiException{
        model.addAttribute("CONTAIN_3_NODES",MainUtils.CONTAIN_3_NODES);
        List<NodeInfo> nodeList = infoService.getNodeInfo();
        session.setAttribute("nodeList",nodeList);
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
    public String toNodesPage(Model model, HttpSession session, @RequestParam int nodeIndex) throws ApiException {
        List<NodeInfo> nodeList = infoService.getNodeInfo();
        String masterName = null;
        model.addAttribute("nodeList",nodeList);

//        修改当前页面中正在访问的node对象
        String desiredName = nodeList.get(nodeIndex).getName();

        session.setAttribute("nodeIndex",nodeIndex);

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
