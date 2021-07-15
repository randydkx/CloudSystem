package cn.edu.njust.utils.publicclouds;

import cn.edu.njust.utils.kubenetecontrol.FuncLib;
import cn.edu.njust.utils.kubenetecontrol.Log;

import java.util.Date;


public class VM {

	public String nodename;// the node name in kubernete cluster
	public VMType vmtype;
	public Date startrenttime;
	public boolean readytorelease=false; //ready to be released at next pricing interval
	public double getCurrentRentalCost()
	{
		long starttime=startrenttime.getTime();
		Date curdate=new Date();
		long curtime=curdate.getTime();
		
		if(curtime<starttime)
		{
			return 0;
		}
		double intervallength=vmtype.priceinterval*1000;
		double dv=(curtime-starttime)/intervallength;
		
		double ceildv=Math.ceil((curtime-starttime)/intervallength);
		double floordv=Math.floor((curtime-starttime)/intervallength);
		int intervals=(int)ceildv;
		if(FuncLib.EqualDouble(dv, floordv, -6))
		{
			intervals=(int)floordv;
		}
		/**
		 * add setup cost 2016.10.28
		 */
		//double setupcost=(this.setuptime/this.intervallength)*priceperinterval;
		
		Log.printLine("dv:"+dv);
		
		Log.printLine(curtime+" curtime:"+curtime+"-starttime:"+starttime+",intervals:"+intervals);
		// setup time has been included in normal intervals 2017 10 12 Czc modified
		//return intervals*priceperinterval+setupcost;
		return intervals*vmtype.price;
		
	}
	/**
	 * get the time to the next pricing point miliseconds
	 * @return
	 */
	public long getNextPricingPoint()
	{
		long starttime=startrenttime.getTime();
		Date curdate=new Date();
		long curtime=curdate.getTime();
		
		double intervallength=vmtype.priceinterval*1000;
		double dv=(curtime-starttime)/intervallength;
		
		double ceildv=Math.ceil((curtime-starttime)/intervallength);
		double floordv=Math.floor((curtime-starttime)/intervallength);
		int intervals=(int)ceildv;
		if(FuncLib.EqualDouble(dv, floordv, -6))
		{
			return 0;
		}
		
		long timetonextpricingpoint=(long) (starttime+intervals*vmtype.priceinterval*1000-curtime);
		return timetonextpricingpoint;
		
	}
}
