package cn.edu.njust.utils.scheduler;

import cn.edu.njust.utils.kubenetecontrol.Log;
import cn.edu.njust.utils.kubenetecontrol.SystemParameter;

import java.math.BigDecimal;
//import scheduler.MMNfeedbackController.ControllerParameters;

public class MMSModeloptimal {
	
	private static int scale=128;
	private static BigDecimal getP0(int s,double r,double u)
	{
		if(r==0)r=1;
		BigDecimal p=BigDecimal.valueOf(r).divide(BigDecimal.valueOf(u),scale, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal result=BigDecimal.valueOf(0);
		for(int i=0;i<s;i++)
		{
			result=result.add(pow(p, BigDecimal.valueOf(i)).divide(factorial(i),scale, BigDecimal.ROUND_HALF_EVEN));
		}
		result=result.add(pow(p,BigDecimal.valueOf(s)).divide(factorial(s).multiply(BigDecimal.valueOf(1).subtract(p.divide(BigDecimal.valueOf(s),scale, BigDecimal.ROUND_HALF_EVEN))),scale, BigDecimal.ROUND_HALF_EVEN));
		result=BigDecimal.valueOf(1).divide(result,scale, BigDecimal.ROUND_HALF_EVEN);
		return result;
	}
	private static BigDecimal getCsp(int s,double r,double u)
	{
		BigDecimal p=BigDecimal.valueOf(r).divide(BigDecimal.valueOf(u),scale, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal p0=getP0(s,r,u);
		BigDecimal result=BigDecimal.valueOf(0);
		//result=Math.pow(p, s)/(factorial(s)*(1-p/s))*p0;
		result=pow(p, BigDecimal.valueOf(s)).multiply(p0).divide(factorial(s).multiply(BigDecimal.valueOf(1).subtract(p.divide(BigDecimal.valueOf(s),scale, BigDecimal.ROUND_HALF_EVEN))),scale, BigDecimal.ROUND_HALF_EVEN);
		return result;
	}
	
	static BigDecimal getLq(int s,double r,double u)
	{
		BigDecimal p=BigDecimal.valueOf(r).divide(BigDecimal.valueOf(u),scale, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal csp=getCsp(s,r,u);
		BigDecimal result=BigDecimal.valueOf(0);
		result=csp.multiply(p).divide(BigDecimal.valueOf(s),scale, BigDecimal.ROUND_HALF_EVEN).divide(BigDecimal.valueOf(1).subtract(p.divide(BigDecimal.valueOf(s),scale, BigDecimal.ROUND_HALF_EVEN)),scale, BigDecimal.ROUND_HALF_EVEN);
		return result;
	}
	
	protected static BigDecimal getTq(int s,double r,double u)
	{
		BigDecimal result=getLq(s,r,u).divide(BigDecimal.valueOf(r),scale, BigDecimal.ROUND_HALF_EVEN);
		return result;
	}
	
	public static BigDecimal getLatency(int s,double r,double u)
	{
		//BigDecimal result=getTq(s,r,u)+1/u;
		if(s*u<=r)
		{
			return BigDecimal.valueOf(Double.MAX_VALUE);
		}
		BigDecimal result=getTq(s,r,u).add(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(u),scale, BigDecimal.ROUND_HALF_EVEN));
		return result;
	}
	
	private static BigDecimal pow(BigDecimal a,BigDecimal b)
	{
		BigDecimal result=BigDecimal.valueOf(1);
		for(int i=0;i<b.intValue();i++)
		{
			result=result.multiply(a);
		}
		return result;
	}
	private static BigDecimal factorial(int n)
	{
		BigDecimal a=BigDecimal.valueOf(1);
		for(int i=1;i<=n;i++)
			a=a.multiply(BigDecimal.valueOf(i));
		return a;
	}
	
//	/**
//	 * Get response time
//	 * @param r arrival rate
//	 * @param vmnum processor number
//	 * @param parameters
//	 * @return
//	 */
//	public static double getResponsetime(double r, int vmnum, ControllerParameters parameters)
//	{
//		if(vmnum==0)
//			return Double.MAX_VALUE;
//		double u=parameters.mu+parameters.c/r;
//		if(u*vmnum<=r)
//		{
//			return Double.MAX_VALUE;
//		}
//		BigDecimal responsetime=getLatency(vmnum,r,u);
//
//		return responsetime.doubleValue();
//
//	}
//
//	public static int getMinVMfordelay(double r,double processtime, ControllerParameters parameters, int queuelength)
//	{
//
//		int head=1;
//
//		if(processtime>0)
//		{
//			//while(getLatency(head,r,u).multiply(BigDecimal.valueOf(3600)).compareTo(BigDecimal.valueOf(latency))==1)
//
//
//
//			double u=parameters.mu+parameters.c/r;
//			while(u*head<=r)
//			{
//				head++;
//				u=parameters.mu+parameters.c/r;
//			}
//			BigDecimal response=getLatency(head,r,u);
//
//
//
//			Log.printLine("VM num="+head+",lamada="+r+",mu="+u+", response="+response);
//			while(response.compareTo(BigDecimal.valueOf(processtime))==1)
//			{
//				head++;
//				u=parameters.mu+parameters.c/r;
//				response=getLatency(head,r,u);
//				Log.printLine("VM num="+head+",lamada="+r+",mu="+u+", response="+response);
//				if(head>=SystemParameter.Maximum_Container)
//					break;
//			}
//			/*double lq=getLq(head,r,u).doubleValue()+r/u;
//			if(queuelength>lq)
//			{
//				Log.printLine("VM num="+head+",lamada="+r+",mu="+u+", Lq="+lq+",queuelength="+queuelength);
//				while((u*head-r)*30<queuelength-lq)
//				{
//					head++;
//					u=parameters.mu+parameters.c/r;
//					lq=getLq(head,r,u).doubleValue()+r/u;
//				}
//
//			}
//			*/
//
//
//		}
		
		
//		return head;
//	}
	
	public static int getMinVMfordelay(double r,double u,double processtime, double delay)
	{
		
		if(processtime>0&&delay>0)
		{
			Log.printLine("processtime and delay cannot be set at the same time");
		}
		int head=(int) (r/u)+1;
		if(processtime>0)
		{
			//while(getLatency(head,r,u).multiply(BigDecimal.valueOf(3600)).compareTo(BigDecimal.valueOf(latency))==1)
			BigDecimal response=getLatency(head,r,u);
			Log.printLine("VM num="+head+",lamada="+r+",mu="+u+", response="+response);
			while(response.compareTo(BigDecimal.valueOf(processtime))==1)
			{
				head++;
				response=getLatency(head,r,u);
				Log.printLine("VM num="+head+",lamada="+r+",mu="+u+", waittime="+response);
				if(head>=SystemParameter.Maximum_Container)
					break;
			}
		}
		else
		{
			BigDecimal curdelay=getTq(head,r,u);
			Log.printLine("VM num="+head+",lamada="+r+",mu="+u+", waittime="+curdelay);
			while(curdelay.compareTo(BigDecimal.valueOf(delay))==1)
			{
				head++;
				curdelay=getTq(head,r,u);
				Log.printLine("VM num="+head+",lamada="+r+",mu="+u+", waittime="+curdelay);
			}
			Log.printLine("VM number="+head);
		}
		
		return head;
	}
	
	public  static int forecast(double r,double u,double latency)
	{
		int head=(int) (r/u)+1;
		//while(getLatency(head,r,u).multiply(BigDecimal.valueOf(3600)).compareTo(BigDecimal.valueOf(latency))==1)
		while(getLatency(head,r,u).compareTo(BigDecimal.valueOf(latency))==1)
		{
			head++;
		}
		return head;
	}
	
	
}
