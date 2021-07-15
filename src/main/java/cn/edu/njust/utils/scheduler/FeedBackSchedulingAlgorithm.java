package cn.edu.njust.utils.scheduler;

import cn.edu.njust.utils.kubenetecontrol.*;
import cn.edu.njust.utils.publicclouds.ClusterManager;
import cn.edu.njust.utils.publicclouds.Deployment;
import cn.edu.njust.utils.publicclouds.VM;
import cn.edu.njust.utils.publicclouds.VMType;

import java.util.ArrayList;

public class FeedBackSchedulingAlgorithm implements IDataCenterScheduler {

	FeedbackController resourcecontroller=null; 
	LogsManager logmanager=null;
	ControlCenter control_center=null;
	public ClusterManager clustermanager=null;
	private Deployment fibanaci_deployment=null;
	
	



	/**
	 * required capacity 
	 */
	private double requiredcapacity=0;
	
	
	public FeedBackSchedulingAlgorithm(LogsManager logmanager, ControlCenter control_center, ClusterManager pclustermanager,Deployment pfibanaci_deployment) {
		super();
		this.logmanager = logmanager;
		this.control_center = control_center;
		this.clustermanager=pclustermanager;
		this.fibanaci_deployment=pfibanaci_deployment;
	}

	@Override
	public boolean Shedule(Event ev) {
		// TODO Auto-generated method stub
		double newrequiredcapacity=requiredcapacity;
		VMType type=fibanaci_deployment.vmtype;
		int originalnum=fibanaci_deployment.containernum;
		
		
		
		boolean firsttime=false;
		if(resourcecontroller==null)
		{
			Log.printLine("Initialize resourcecontroller......");
			switch (SystemParameter.controltype)
			{
				case MMK_Adjust:
						resourcecontroller=new MMN_Adjust(SystemParameter.SLA*0.6);
					break;
				case MMKForward_FeedbackOriginal:
						resourcecontroller=new MMN_FeedBackOriginal(SystemParameter.SLA*0.6);
					break;
				case FeedBack_Inverse:
				{
					
						//c1= -8.929181175306596 c2= 57132560.092223205 c3= 5475.97442710393 p= 0.4046743546665229
					//c1= 0.08732761159505699 c2= 128.2420342617186 c3= 1300.178269581667 p= 0.2972085842853312
						double c1=0.08732761159505699;
						double c2=128.2420342617186;
						double c3=1300.178269581667;
						double p=0.2972085842853312;
						double afa=0.95;
						// this controller use response time to control
//						resourcecontroller=new OppositeFeedbackController(SystemParameter.SLA*0.5,c1,c2,
//								c3,p,afa,20,120);
					break;
				}
				case FeedBack_MMK:
//					resourcecontroller=new MMNfeedbackController(SystemParameter.SLA);
					break;
				default:
					break;
			}
			firsttime=true;
			Log.printLine("Finish initialize resourcecontroller!");
		}
		//this.logmanager.
		
		resourcecontroller.updateParameters(logmanager.latestlogfile);
		
		double existingcontainercapacity=type.processability*fibanaci_deployment.containernum;
		double averageresponsetime=0;
		ArrayList<Double> times=logmanager.getResponseTimes(3);
		
		
		if(times.size()==1)
			averageresponsetime=times.get(0);
		else if(times.size()>1)
		{
			double filtered=0;
			double rate=0.8;
			for(int i=0;i<times.size();i++)
			{
				double cur=times.get(i);
				if(i==0)
				{
					filtered=cur;
				}
				else
					filtered=filtered*(1-rate)+cur*rate;
			}
			
			averageresponsetime=filtered;
		}
		else
		{
			return false;
		}
		double arrivalrate=logmanager.getArrivalRates(1).get(0);
		if(arrivalrate==0)
			return true;
		double averagedelay=averageresponsetime-SystemParameter.averageprocessingtime;
		Log.printLine("existingcapacity="+existingcontainercapacity+",requiredcapacity="+requiredcapacity);
		if(averagedelay>0)
		{
			newrequiredcapacity=resourcecontroller.getrequiredCapacity(averageresponsetime, arrivalrate, existingcontainercapacity,type.processability,logmanager.getQueuelength());
			/*if(averagedelay<SystemParameter.SLA)
			{
				double nextdesttotalprocessrate=type.processability*(fibanaci_deployment.containernum-1);//next time processing ability
				if(nextdesttotalprocessrate<arrivalrate)//if check release and resource has not been released completely
				{
					
					Log.printLine("Skip feedback scheduling because nextdesttotalprocessrate<arrivalrate leading to unstable system!");
					
					
					newrequiredcapacity=requiredcapacity;
					
				}
				else
				{
					Log.printLine("Feedback scheduling!");
					
					
					newrequiredcapacity=resourcecontroller.getrequiredCapacity(averageresponsetime, arrivalrate, existingcontainercapacity,type.processability,logmanager.getQueuelength());
				}
			}
			else
			{
				Log.printLine("Feedback scheduling!");
				
				
				newrequiredcapacity=resourcecontroller.getrequiredCapacity(averageresponsetime, arrivalrate, existingcontainercapacity,type.processability,logmanager.getQueuelength());
			}
		*/
		
			/**
			 * Adjust resources
			 */
			
			int i=1;
			while(i*type.processability<newrequiredcapacity)
			{
				i++;
			}
			fibanaci_deployment.containernum=i;
			if(fibanaci_deployment.containernum>SystemParameter.Maximum_Container)
				fibanaci_deployment.containernum=SystemParameter.Maximum_Container;
			
			requiredcapacity=fibanaci_deployment.containernum*type.processability;
			Log.printLine("Setting the container number from"+originalnum+" to "+fibanaci_deployment.containernum);
			clustermanager.setContainerCount("default", fibanaci_deployment.name, fibanaci_deployment.containernum);
		}
	
		return true;
	}

	
	
	
	@Override
	public boolean PricingInterval(Event ev) {
		// TODO Auto-generated method stub
		VM vm=(VM) ev.data;
		//double remainedcapacity=this.clustermanager.getTotalVMCapacity()-vm.vmtype.processability;
		//if(remainedcapacity>requiredcapacity)
		//{
		//	clustermanager.ReleaseVM(vm);
		//}
		
		if(this.clustermanager.getRentedVMs().contains(vm)&&vm.readytorelease)
		{
			Log.printLine("Release VM actually:"+vm.nodename);
			clustermanager.ReleaseVM(vm);	
			return true;
		}
		return false;
	}
	

}
