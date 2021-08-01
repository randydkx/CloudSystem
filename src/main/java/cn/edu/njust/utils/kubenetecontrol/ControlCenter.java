package cn.edu.njust.utils.kubenetecontrol;

import cn.edu.njust.utils.kubenetecontrol.SystemParameter.EventType;
import cn.edu.njust.utils.publicclouds.ClusterManager;
import cn.edu.njust.utils.publicclouds.Deployment;
import cn.edu.njust.utils.publicclouds.VM;
import cn.edu.njust.utils.publicclouds.VMType;
import cn.edu.njust.utils.scheduler.FeedBackSchedulingAlgorithm;
import cn.edu.njust.utils.scheduler.IDataCenterScheduler;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
//import scheduler.UnequalMMNSchedulingAlgorithm;

public class ControlCenter {
	LogsManager logmanager = null;
	public EventManager manager = new EventManager();
	IDataCenterScheduler scheduler = null;
	public ClusterManager clustermanager = null;
	public Deployment fibanaci_deployment=new Deployment();
	public boolean readingoldlog=true;
	public static PrintStream print=null;
	public static void main(String[] args) throws InterruptedException {

		//double uptime1=MMSModeloptimal.getLatency(5,100,20).doubleValue();
		
		Date date=new Date();
//		设置日志相关信息
		String datestr=date.toString().replace(":", "-");
		String outputfilename=SystemParameter.database+"\\logs\\ConsoleLog-"+datestr+".json";
  		outputfilename=outputfilename.replace(" ", "");
  		
		try {
			print = new PrintStream(outputfilename);
			Log.setOutput(print);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		ControlCenter controlcenter = new ControlCenter();
		controlcenter.logmanager = new LogsManager(controlcenter);
		controlcenter.clustermanager = new ClusterManager(controlcenter);
		
		VMType type=controlcenter.clustermanager.getVmtypelist().get(0);
		controlcenter.fibanaci_deployment.vmtype=type;
		controlcenter.fibanaci_deployment.name="rubis-deployment";
		controlcenter.fibanaci_deployment.containernum=SystemParameter.Maximum_Container;
		
		//启动读日志线程
		Thread t = new Thread(controlcenter.logmanager);
		t.start();

		// return ;
		// logmanager.getRequestshistory();

		// logmanager.processErrorLogs("");
		// logmanager.readLogs();

		// Init the environment to rent one VM
		
		 
		  
		  //controlcenter.clustermanager.RemoveAll(); //临时删除
		  
		 
		 
		  controlcenter.clustermanager.RentVMs(type, 1);
		  //controlcenter.clustermanager.RemoveNode("czckuber1");
		  //type=controlcenter.clustermanager.getVmtypelist().get(1);
		  //controlcenter.clustermanager.RentVMs(type, 2);
		  SystemParameter.refprocessability=type.processability;
		  //controlcenter.clustermanager.setContainerCount("default", "rubis-deployment", 0);
		 // Thread.sleep(1000);
		  controlcenter.clustermanager.setContainerCount("default", "rubis-deployment", 1);
//		  controlcenter.clustermanager.setContainerCount("default", "rubis-deployment", SystemParameter.Maximum_Container);
		  
		  while(controlcenter.readingoldlog)
		  {
			  Log.printLine("Reading old logs.....");
			  Thread.sleep(10000);
		  }
		  Log.printLine("Finish reading old logs");
		  Event newev =new Event(); 
		  Date curtime=new Date(); 
		  long newtime=curtime.getTime()+(SystemParameter.controlinterval+60)*1000;
		  curtime.setTime(newtime); 
		  newev.time=curtime;
		  newev.tag=EventType.Schedule_Interval;
		  controlcenter.manager.addEvent(newev);
		  
		 /**
			 * Monitoring the system
			 */
				  controlcenter.Monitor();
				  
		  controlcenter.logmanager.setExecuting(false);
		if(print!=null)	 
			print.close();
	}

	private void Monitor() {

		// ControlCenter center=new ControlCenter();
		// center.RemoveNode("czcworker1");
		// center.AddNode("czcworker1");
		
		Date fininshtime=new Date();
		long finishtimemilli=fininshtime.getTime()+SystemParameter.testhours*3600*1000;
		fininshtime.setTime(finishtimemilli);
		Date curtime=new Date();
		while (curtime.before(fininshtime)) {
			long sleeptime = manager.getNextSleepTime();
			try {
				if(sleeptime>0)
				{
					Log.printLine("sleeptime:"+sleeptime);
					Thread.sleep(sleeptime);
				}
				else
				{
					Log.printLine("sleeptime<=0:"+sleeptime);
				}
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			print.flush();
			ArrayList<Event> nexteventlist = manager.getNextEvents();

			for (int i = 0; i < nexteventlist.size(); i++) {
				Event cur_event = nexteventlist.get(i);
				switch (cur_event.tag) {
				// Resource characteristics request
				case Schedule_Interval:
					Shedule(cur_event);
					break;
				case Pricing_Interval:
					PricingInterval(cur_event);
					break;
				}
			}
			curtime=new Date();
			double minutesleft=(fininshtime.getTime()-curtime.getTime())/(1000*60);
			Log.printLine("Time Left: "+minutesleft+" minutes........................");
		}

	}

	/**
	 * called whenever at the pricing point
	 * 
	 * @param ev
	 * @return
	 */
	private boolean PricingInterval(Event ev) {
		boolean released=false;
		switch (SystemParameter.controltype) {
		case MMK_Adjust: {
			if (scheduler == null) {
				scheduler = new FeedBackSchedulingAlgorithm(this.logmanager, this, this.clustermanager,this.fibanaci_deployment);
				
				
			}
			released=scheduler.PricingInterval(ev);

			break;
		}
		case MMKForward_FeedbackOriginal: {
			if (scheduler == null) {
				scheduler = new FeedBackSchedulingAlgorithm(this.logmanager, this, this.clustermanager,this.fibanaci_deployment);
				

			}
			released=scheduler.PricingInterval(ev);
			break;
		}
		case MMK_Unequal:
			if (scheduler == null) {
//				scheduler = new UnequalMMNSchedulingAlgorithm(this.logmanager, this, this.clustermanager);
				
				
			}
			released=scheduler.PricingInterval(ev);
			break;
		case FeedBack_Inverse:
			if (scheduler == null) {
				scheduler = new FeedBackSchedulingAlgorithm(this.logmanager, this, this.clustermanager,this.fibanaci_deployment);
				
				
			}
			released=scheduler.PricingInterval(ev);
			break;
		case FeedBack_MMK:
			if (scheduler == null) {
				scheduler = new FeedBackSchedulingAlgorithm(this.logmanager, this, this.clustermanager,this.fibanaci_deployment);
				
				
			}
			released=scheduler.PricingInterval(ev);
			break;
		default:
			break;
		}
		if(!released)
		{
			VM vm = (VM) ev.data;
	
			Event newev = new Event();
			Date curtime = new Date();
			long newtime = (long) (curtime.getTime() + vm.vmtype.priceinterval * 1000);
			curtime.setTime(newtime);
			newev.time = curtime;
			newev.data = vm;
			newev.tag = EventType.Pricing_Interval;
			manager.addEvent(newev);
		}
		return true;
	}

	/**
	 * Schedule the system
	 * 
	 * @return
	 */
	private boolean Shedule(Event ev) {
		// double requiredcapacity=-1;
		logmanager.SaveLogstoJson(); //save logs
		this.clustermanager.UpdateContainerCost();//change container cost;
		//clustermanager.getRental();
		switch (SystemParameter.controltype) {
		case MMK_Adjust: {
			if (scheduler == null) {
				scheduler = new FeedBackSchedulingAlgorithm(this.logmanager, this, this.clustermanager,this.fibanaci_deployment);
				
				
			}
			scheduler.Shedule(ev);

			break;
		}
		case MMKForward_FeedbackOriginal: {
			if (scheduler == null) {
				scheduler = new FeedBackSchedulingAlgorithm(this.logmanager, this, this.clustermanager,this.fibanaci_deployment);
				
				
			}
			scheduler.Shedule(ev);
			break;
		}
		case MMK_Unequal:
			if (scheduler == null) {
//				scheduler = new UnequalMMNSchedulingAlgorithm(this.logmanager, this, this.clustermanager);
				
				
			}
			scheduler.Shedule(ev);
			break;
		case FeedBack_Inverse: 
			if (scheduler == null) {
				scheduler = new FeedBackSchedulingAlgorithm(this.logmanager, this, this.clustermanager,this.fibanaci_deployment);
				
				
			}
			scheduler.Shedule(ev);
			break;
		case FeedBack_MMK:
			if (scheduler == null) {
				scheduler = new FeedBackSchedulingAlgorithm(this.logmanager, this, this.clustermanager,this.fibanaci_deployment);
				
				
			}
			scheduler.Shedule(ev);
			break;
		
		default:
			break;
		}
		Event newev = new Event();
		Date curtime = new Date();
		long newtime = curtime.getTime() + SystemParameter.controlinterval * 1000;
		curtime.setTime(newtime);
		newev.time = curtime;
		newev.tag = EventType.Schedule_Interval;
		manager.addEvent(newev);

		return true;
	}

}
