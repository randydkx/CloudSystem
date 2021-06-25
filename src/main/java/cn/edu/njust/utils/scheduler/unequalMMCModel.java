package cn.edu.njust.utils.scheduler;

import cn.edu.njust.utils.kubenetecontrol.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

public class unequalMMCModel {
	
	private int c;         
	private double r;      
	private ArrayList<Double> u;   
	private double P0=-1;
	private static int scale=128;
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	public ArrayList<Double> getU() {
		return u;
	}
	public void setU(ArrayList<Double> u) {
		
		this.u = sortIndesendingorder(u);
		this.c=u.size();
	}
	
	/**
	 * order u in descending order
	 * @param u
	 * @return
	 */
	private ArrayList<Double> sortIndesendingorder(ArrayList<Double> u)
	{
		ArrayList<Double> orderedu=new ArrayList<Double>();
		for(int i=0;i<u.size();i++)
		{
			boolean added=false;
			for(int j=0;j<orderedu.size();j++)
			{
				if(u.get(i)>orderedu.get(j))
				{
					orderedu.add(j,u.get(i));
					added=true;
					break;
				}
			}
			if(!added)
			{
				orderedu.add(u.get(i));
			}
			
		}
		return orderedu;
	}
	private BigDecimal pow(BigDecimal a,BigDecimal b)
	{
		BigDecimal result=BigDecimal.valueOf(1);
		for(int i=0;i<b.intValue();i++)
		{
			result=result.multiply(a);
		}
		return result;
	}
	/**
	 * the sum of mu1 to muk
	 * @param k
	 * @return
	 */
	private double getCumulatedMu(int k)
	{
		double cumu=0;
		for(int i=0; i<k; i++) 
		{
			cumu+=u.get(i);
		}
		return cumu;
	}
	
	/**
	 * Calculating P0
	 * @return
	 */
	private double getP0() {
		BigDecimal sum=BigDecimal.valueOf(1);
		for(int i=1; i<c; i++)
		{
			BigDecimal denominator=BigDecimal.valueOf(1);
			for(int j=1;j<=i;j++)
			{
				//denominator*=getCumulatedMu(j);
				denominator=denominator.multiply(BigDecimal.valueOf(getCumulatedMu(j)));
			}
			//sum+=Math.pow(r, i)/denominator;
			sum=sum.add(pow(BigDecimal.valueOf(r),BigDecimal.valueOf(i)).divide(denominator,scale, BigDecimal.ROUND_HALF_EVEN));
		}
		
		BigDecimal denominator2=BigDecimal.valueOf(1);
		for(int i=1; i<c; i++)
		{
			//denominator2*=getCumulatedMu(i);
			denominator2=denominator2.multiply(BigDecimal.valueOf(getCumulatedMu(i)));
		}
		//denominator2*=(getCumulatedMu(c)-r);
		denominator2=denominator2.multiply(BigDecimal.valueOf(getCumulatedMu(c)-r));
		
		//sum+=Math.pow(r, c)/denominator2;
		sum=sum.add(pow(BigDecimal.valueOf(r),BigDecimal.valueOf(c)).divide(denominator2,scale, BigDecimal.ROUND_HALF_EVEN));
		
		P0=BigDecimal.valueOf(1).divide(sum,scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
		Log.printLine("P0="+P0);
		return P0;
	}
	
	
	//average waiting request
	public double getLq()
	{
		double rou=r/getCumulatedMu(c);
		
		//double denominator=1;
		BigDecimal denominator=BigDecimal.valueOf(1);
		for(int i=1; i<=c; i++)
		{
			//denominator*=getCumulatedMu(i);
			denominator=denominator.multiply(BigDecimal.valueOf(getCumulatedMu(i)));
		}
		
		//double xishu=rou*Math.pow(r, c)/(Math.pow(1-rou, 2)*denominator);
		
		BigDecimal xishu=pow(BigDecimal.valueOf(r),BigDecimal.valueOf(c)).multiply(BigDecimal.valueOf(rou)).divide(pow(BigDecimal.valueOf(1-rou),BigDecimal.valueOf(2)).multiply(denominator),scale, BigDecimal.ROUND_HALF_EVEN);
		double Lq=xishu.doubleValue()*getP0();
		return Lq;
		
	}
	//average number of processing requests
	public double getLf()
	{
		if(P0==-1)
		{
			getP0();
		}
		BigDecimal sum=BigDecimal.valueOf(0);
		for(int i=1; i<=c; i++)
		{
			//double denominator=1;
			BigDecimal denominator=BigDecimal.valueOf(1);
			for(int j=1;j<=i;j++)
			{
				//denominator*=getCumulatedMu(j);
				denominator=denominator.multiply(BigDecimal.valueOf(getCumulatedMu(j)));
			}
			//sum+=i*Math.pow(r, i)/denominator;
			BigDecimal numerator=pow(BigDecimal.valueOf(r),BigDecimal.valueOf(i)).multiply(BigDecimal.valueOf(i));
			sum=sum.add(numerator.divide(denominator,scale, BigDecimal.ROUND_HALF_EVEN));
			
		}
		double rou=r/getCumulatedMu(c);
		BigDecimal denominator2=BigDecimal.valueOf(1);
		for(int i=1; i<=c; i++)
		{
			//denominator2*=getCumulatedMu(i);
			denominator2=denominator2.multiply(BigDecimal.valueOf(getCumulatedMu(i)));
		}
		
		BigDecimal bigc=BigDecimal.valueOf(c);
		BigDecimal numerator=bigc.multiply(pow(BigDecimal.valueOf(r),BigDecimal.valueOf(c))).multiply(BigDecimal.valueOf(rou));
		
		denominator2=denominator2.multiply(BigDecimal.valueOf(1-rou));
		BigDecimal part2=numerator.divide(denominator2,scale, BigDecimal.ROUND_HALF_EVEN);
		sum=sum.add(part2);
		
		
		double Lf=sum.doubleValue()*P0;
		
		return Lf;
	}
	/**
	 * get expectation of waiting and processing requests
	 * @return
	 */
	private double getLs()
	{
		return getLq()+getLf();
	}
	
	//average response time including processing time
	public double getWs()
	{
		double result=getLs()/r;
		return result;
	}
	public double getResponsetime(ArrayList<Double> u, double r, double currentquelength, double duration)
	{
		
		this.setU(u);
		this.setR(r);
		P0=-1;
		double total_u=0;
		for(int i=0;i<u.size();i++)
		{
			total_u+=u.get(i);
		}
		double c=u.size();
		if(r>=total_u)//not stable state
		{
			
			double q_real=currentquelength+(duration/2)*(r-total_u);
			double responsetime=(q_real+c)/total_u;
			return responsetime;//including processing time
		}
		else
		{
			double Lq=getLq();
			Log.printLine("Lq="+Lq);
			double Lf=getLf();
			Log.printLine("Lf="+Lf);
			if(currentquelength<=Lq)// using default queue length
			{
				return getWs();
			}
			else// queue length is decreasing
			{
				double detat=(currentquelength-Lq)/(total_u-r);
				double tba=Math.min(duration, detat);
				double q_real=(1/duration)*((currentquelength+tba*(r-total_u)/2)*tba+Lq*(duration-tba));
				double responsetime=(q_real+Lf)/r;
				return responsetime;//including processing time
			}
			
		}
	}
	public static void main(String[] args) throws Exception {
	    // Create the linear solver with the GLOP backend.
		 
		ArrayList<Double> u=new ArrayList<Double>();
		u.add(40d);
		u.add(40d);
		u.add(20d);
		u.add(20d);
		double r=30;
		double currentquelength=140;
		double duration=180;
		unequalMMCModel umodel=new unequalMMCModel();
		double responsetime=umodel.getResponsetime(u, r, currentquelength, duration);
		Log.printLine(responsetime);
		r=50;
		
		responsetime=umodel.getResponsetime(u, r, currentquelength, duration);
		Log.printLine(responsetime);
		
		r=100;
		
		responsetime=umodel.getResponsetime(u, r, currentquelength, duration);
		Log.printLine(responsetime);
		
		r=118;
		
		responsetime=umodel.getResponsetime(u, r, currentquelength, duration);
		Log.printLine(responsetime);
	  }
}
