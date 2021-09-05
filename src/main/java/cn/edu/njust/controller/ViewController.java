package cn.edu.njust.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 视图控制器，用于控制视图之间的跳转
 */
@Controller
@RequestMapping(value = "/to")
public class ViewController {

    @RequestMapping("/index")
    public String toIndexPage(){
        return "page/index";
    }

    @RequestMapping("/deployment")
    public String toDeploymentPage(){
        return "page/deployment";
    }

    @RequestMapping("/load")
    public String toLoadPage(){
        return "page/load";
    }

    @RequestMapping("/nodes")
    public String toNodesPage(){
        return "page/nodes";
    }
}
