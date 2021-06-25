package cn.edu.njust.utils.scheduler;

import cn.edu.njust.utils.kubenetecontrol.Log;

import java.util.ArrayList;

public class MMN_FeedBackOriginal implements FeedbackController {

	private double pole=0.6;
	public MMN_FeedBackOriginal(double r) {
		super();
		R = r;
		setParameters(pole, R);
	}
	private double g=0, r=0;
	
	private void setParameters(double pole, double R)
	{
		g = -1;

		r = (pole*(1-Math.pow(R, 2)*g)-1)/(-Math.pow(R, 2)*g);

	}
	/**
	 * Refereence point
	 */
	private double R=0;
	@Override
	public void setR(double r) {
		// TODO Auto-generated method stub
		if(r!=R)
		{
			R = r;
			setParameters(pole, R);
			ukm1=0;
			ekm1=0;
		}
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

	@Override
	public void updateParameters(String tracepath) {
		// TODO Auto-generated method stub
		
	}
	double ukm1=0;
	double ekm1=0;
	
	double previouscapacityrequest=0;
	double lamdaadjustrate=1;
	@Override
	/**
	 * rk is the arrive rate of each second
	 */
	public double getrequiredCapacity(double responsetime, double rk, double currentcapacity, double refprocessability, ArrayList<Double> queuelengths) {
		// TODO Auto-generated method stub
		double averagedelay=responsetime;// current algorithm is for response not average delay
		
		states.clear();
		//Log.enable();
		Log.printLine("/////////////////////////////////MMN_FeedBackOriginal");
		
		Log.printLine("averagedelay="+averagedelay);
		//rk=rk/WEBAPPsetting.stepinterval;
		//double upthreshold=R*20, downshreshold=R * 0.3;
		//Log.printLine("waiting time upthreshold="+upthreshold+", downshreshold="+downshreshold+", averageprocessingtime="+SystemParameter.averageprocessingtime);
		/*
		 * if(rk!=0) { if (averagedelay >upthreshold) {
		 * 
		 * averagedelay=R;
		 * 
		 * } else if (averagedelay <downshreshold) { //该删的资源已删掉 //if(!CanReleased())
		 * {//Check wheather there is averagedelay=R; } }
		 */
		//double upthreshold=R*1.05;
		//double downshreshold=R*0.3;
		//if (averagedelay<upthreshold && averagedelay >downshreshold) {
		//	averagedelay=R;
		//}
		//Log.printLine("changed averagedelay="+averagedelay);
		
		if(averagedelay>R*5)
			averagedelay=R*5;
		else if(averagedelay<R*0.5)
		{
			averagedelay=R*0.5;
		}
		
		
		//using M/M/N model to calculate minimum number of VM for the type chosenconfig
		int minvm=MMSModeloptimal.getMinVMfordelay(rk, refprocessability, R,0);
		
		//using linear model derived from MM1
		double ek=0-(averagedelay-R);
		Log.printLine("trimed averagedelay="+averagedelay+",g="+g+",r="+r+",ek="+ek+",ekm1="+ekm1+",ukm1="+ukm1);
		double uk=ukm1+g*(ek-r*ekm1);
		double changevm=uk/refprocessability;
		double newvmnum=(double)minvm+changevm;
		//newvmnum=Math.min(WEBAPPsetting.Maximum_VMnum,newvmnum);
		newvmnum=Math.max(1,newvmnum);
		Log.printLine("ukm1="+ukm1);
		ukm1=uk;
		ekm1=ek;
		
		
		Log.printLine("uk="+uk);
		Log.printLine("changevm="+changevm);
		Log.printLine("newvmnum="+newvmnum);
		double ck=newvmnum*refprocessability;
		states.add(ck);
		Log.printLine("ck="+ck);
		//Log.disable();
		return ck;
	}
	

}
