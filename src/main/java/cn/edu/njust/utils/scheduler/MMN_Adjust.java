package cn.edu.njust.utils.scheduler;

import cn.edu.njust.utils.kubenetecontrol.Log;
import cn.edu.njust.utils.kubenetecontrol.SystemParameter;

import java.util.ArrayList;


public class MMN_Adjust implements FeedbackController {

	
	@Override
	public void updateParameters(String tracepath) {
		// TODO Auto-generated method stub
		
	}
	public MMN_Adjust(double r) {
		super();
		R = r;
		assignParameters(0.6, 0.1);
	}
	/**
	 * Refereence point
	 */
	private double R=0;
	@Override
	public void setR(double r) {
		// TODO Auto-generated method stub
		R = r;
	}
	/**
	 * store states of current step
	 */
	private ArrayList<Double> states=new ArrayList<Double>();
	@Override
	public ArrayList<Double> getState() {
		// TODO Auto-generated method stub
		return states;
	}
	/**
	 * P1=x+yi
	 * P2=x-yi
	 * P3=p3
	 * @param x
	 * @param y
	 * @param
	 */
	private void assignParameters(double x, double y)
	{
		g=-2*x+2;
		r=(Math.pow(x, 2)+Math.pow(y, 2)-1)/(-g);
	}
	private double g=0, r=0;
	private double ukm1=0;
	private double ekm1=0;
	double previouscapacityrequest=0;
	double lamdaadjustrate=1;
	
	
	@Override
	public double getrequiredCapacity(double responsetime, double rk, double currentcapacity, double refprocessability, ArrayList<Double> queuelengths) {
		// TODO Auto-generated method stub
		states.clear();
		
		//double averagedelay=responsetime-(1/refprocessability);
		//double averagedelay=responsetime-SystemParameter.averageprocessingtime;
		double averagedelay=responsetime;// changed for response time rather than average delay
		if(averagedelay<0)
			averagedelay=0;
		Log.printLine("/////////////////////////////////MMN_Adjust");
		
		Log.printLine("averagedelay="+averagedelay);
		
		/*if(averagedelay>R*1.5)
			averagedelay=R*1.5;
		else if(averagedelay<R*0.5)
		{
			averagedelay=R*0.5;
		}

		*/
		states.add(currentcapacity);
		states.add(responsetime);
		double upthreshold=R*1.25, downshreshold=R * 0.75;
		Log.printLine("waiting time upthreshold="+upthreshold+", downshreshold="+downshreshold+", averageprocessingtime="+SystemParameter.averageprocessingtime);
		//if(averagedelay>upthreshold)
		//			averagedelay=upthreshold;
		//else if(averagedelay<downshreshold)
		//{
		//	averagedelay=downshreshold;
		//}
		double mu=(currentcapacity/refprocessability)*refprocessability;
		double ek=0;
		if(rk!=0)
		{
			//时延在0.78-0.90之间是正常的，大于0.90则扩大系数，小于0.78则缩小系数
			double ratio=0.0;
			
			//double upthreshold=R, downshreshold=R;
			if (averagedelay >upthreshold) {
				
				ek=R-averagedelay;
			
			} else if (averagedelay <downshreshold) {
				//该删的资源已删掉
				//if(!CanReleased()) {//Check wheather there is 
				ek=R-averagedelay;	
			}
		}

		double Kp=1; //Kp=0.2, 0.4, 0.6, 0.8, 0.95

		
		//double u=ukm1+g*(ek-r*ekm1);
		double u=Kp*ek;
		//if(averagedelay<R*0.01)
		//	averagedelay=R*0.01;
		double desty=u+averagedelay;
		//using MM1 model to derive the real 
		Log.printLine("averagedelay="+averagedelay+",desty="+ desty);
		
		//for average delay
		//double reallamada=Math.pow(mu, 2)*averagedelay/(1+mu*averagedelay);
		//double theorylamada=Math.pow(mu, 2)*(desty)/(1+mu*desty);
		//for response time
		double reallamada=mu-(1/averagedelay);
		double theorylamada=mu-(1/desty);
		Log.printLine("reallamada"+reallamada+",theorylamada="+ theorylamada);
		double ratio=reallamada/theorylamada;
		Log.printLine("ratio"+ratio);
		if (ratio>2)
			ratio=2;
		else if(ratio<0.9)
			ratio=0.9;
		lamdaadjustrate = lamdaadjustrate*ratio;
		Log.printLine("Change WEBAPPsetting.lamdaadjustrate by"+ratio+",="+ lamdaadjustrate);
		
		states.add(ratio);
		states.add(lamdaadjustrate);
		//ukm1=u;
		//ekm1=ek;
		
		//find action
		 
		rk=rk*lamdaadjustrate;
		//using M/M/N model to calculate minimum number of VM for the type chosenconfig
		int minvm=MMSModeloptimal.getMinVMfordelay(rk, refprocessability, R, 0);
		//minvm=Math.min(WEBAPPsetting.Maximum_VMnum,minvm);
		Log.printLine("minvm="+minvm);
		double ck=minvm*refprocessability;
		
		Log.printLine("ck="+ck);
		return ck;
	}
	

}
