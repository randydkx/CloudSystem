package cn.edu.njust.utils.kubenetecontrol;


import cn.edu.njust.entity.Request;

import java.util.ArrayList;
import java.util.List;

public class SystemParameter {

	public static long controlinterval=250;
	public static long pricinginterval=600;
	public static String nginxstatusurl="http://192.168.7.20:31597/nginx_status";
	public static double SLA=0.1;
	public static double averageprocessingtime=0;
	public static double refprocessability=20;
	
	public static int averagerequesteachuser=1;
	public static long testhours=24;
	public static String database="D:\\Runspace";
	public static boolean isCheckReleased=true;
	public static int Maximum_Container=1;
	public enum ResourceControltype {

        FeedBack_Inverse, Deeplearning, FeedBack_MMK, FeedBack_Linear, MMK_Pure, MMK_Adjust, MMKForward_FeedbackOriginal, MMK_Unequal
    }
	public enum EventType {

        Schedule_Interval, Pricing_Interval
    }
	public static ResourceControltype controltype= ResourceControltype.FeedBack_MMK;
}
