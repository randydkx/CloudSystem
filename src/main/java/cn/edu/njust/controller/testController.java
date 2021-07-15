package cn.edu.njust.controller;

import cn.edu.njust.entity.*;
import com.alibaba.fastjson.JSONObject;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.edu.njust.entity.*;
import cn.edu.njust.utils.publicclouds.*;
import cn.edu.njust.utils.kubenetecontrol.*;

@Controller
@RequestMapping(value = "/test")
public class testController {

    @RequestMapping(value = "/fun")
    public String fun() throws InterruptedException {
        System.out.println("helloworld");
//        CoreV1Api coreApi = Connect2System.getAPI();
        return "page/test";
    }
}
