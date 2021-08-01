package cn.edu.njust.utils.publicclouds;

import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.PatchUtils;
import cn.edu.njust.utils.kubenetecontrol.Connect2System;
import cn.edu.njust.utils.kubenetecontrol.ControlCenter;
import cn.edu.njust.utils.kubenetecontrol.Event;
import cn.edu.njust.utils.kubenetecontrol.Log;
import cn.edu.njust.utils.kubenetecontrol.SystemParameter.EventType;

import java.util.ArrayList;
import java.util.Date;

public class ClusterManager {

	ControlCenter control_center=null;
	//DataCenterAgent dcagent=null;
	ArrayList<DataCenterAgent> dcagentlist=new ArrayList<DataCenterAgent>();
	private double existingrental=0;//rental cost of released VMs

	public ClusterManager(ControlCenter pcontrol_center) {
		super();
		this.control_center=pcontrol_center;
		/**
		 * Use a local data center
		 */
		DataCenterAgent dcagent=new LocalClusterAgent();
		dcagentlist.add(dcagent);
		
		
		//initialize
		for(int i=0;i<dcagentlist.size();i++)
		{
			DataCenterAgent agent=dcagentlist.get(i);
			vmtypelist.addAll(agent.getVMTypes());
			
			/*for(int j=0;j<agent.getVMTypes().size();j++)
			{
				VMType type=agent.getVMTypes().get(j);
				ArrayList<String> nodes=agent.getNodemaps().get(type);
				for(int k=0;k<nodes.size();k++)
				{
					String nodename=nodes.get(k);
					RemoveNode(nodename);
				}
			}
			*/
		}
		
	}
	public void RemoveAll()
	{
		for(int i=0;i<dcagentlist.size();i++)
		{
			DataCenterAgent agent=dcagentlist.get(i);

			for(int j=0;j<agent.getVMTypes().size();j++)
			{
				VMType type=agent.getVMTypes().get(j);
				ArrayList<String> nodes=agent.getNodemaps().get(type);
				for(int k=0;k<nodes.size();k++)
				{
					String nodename=nodes.get(k);
					RemoveNode(nodename);
				}
			}
			
		}
		
	}
	public void RentAll()
	{
		for(int i=0;i<vmtypelist.size();i++)
		{
			VMType type=vmtypelist.get(i);
			int left=type.count_limit-getExistingVMs(type);
			RentVMs(type,left);
		}
	}
	/**
	 * The list of all available vmtypes
	 */
	private ArrayList<VMType> vmtypelist=new ArrayList<VMType>();
	
	/**
	 * List of rented VMs
	 */
	private ArrayList<VM> rentedVMs=new ArrayList<VM>();
	
	
	/**
	 * get all available VMTypes
	 * @return
	 */
	public ArrayList<VMType> getVmtypelist() {
		return vmtypelist;
	}


	/**
	 * Get all rented VMs
	 * @return
	 */
	public ArrayList<VM> getRentedVMs() {
		return rentedVMs;
	}
	
	/**
	 * get the total capacity of VMs
	 * @return
	 */
	public double getTotalVMCapacity()
	{
		double totalcapacity=0;
		for(int i=0;i<rentedVMs.size();i++)
		{
			VM tempvm=rentedVMs.get(i);
			totalcapacity+=tempvm.vmtype.processability;
		}
		return totalcapacity;
	}

	/**
	 * Get available VMs for the specified VM type according to the limitation of public clouds
	 * @param type
	 * @return
	 */
	public int getExistingVMs(VMType type)
	{
		int count=0;
		for(int i=0;i<rentedVMs.size();i++)
		{
			VM tempvm=rentedVMs.get(i);
			if(tempvm.vmtype==type)
			{
				count++;
			}
		}
		
		return count;
	}
	
	
	
	/**
	 * Rent new VMs of VMType type
	 * return VM with  node names after added to the kubernete cluster
	 * @param type
	 * @param count
	 * @return
	 */
	public ArrayList<VM> RentVMs(VMType type, int count)
	{
		ArrayList<String> kubectlnodenames=null;
		for(int i=0;i<dcagentlist.size();i++)
		{
			DataCenterAgent agent=dcagentlist.get(i);
			if(agent.getVMTypes().contains(type))
			{
				kubectlnodenames=agent.rentVMfromDaterCenter(type, count);
			}
		}
		ArrayList<VM> newrentedVMlist=new ArrayList<VM>();
		for(int i=0;i<kubectlnodenames.size();i++)
		{
			String nodename=kubectlnodenames.get(i);
			Date curtime=new Date();
			
			VM vm=new VM();
			vm.nodename=nodename;
			vm.vmtype=type;
			vm.startrenttime=curtime;
			newrentedVMlist.add(vm);
			
			Event newev =new Event();
			
			long newtime=(long) (curtime.getTime()+vm.vmtype.priceinterval*1000);
			Date pricingtime=new Date();
			pricingtime.setTime(newtime);
			newev.time=pricingtime;
			newev.data=vm;
			newev.tag=EventType.Pricing_Interval;
			control_center.manager.addEvent(newev);
			
			AddNode(nodename);
		}
		rentedVMs.addAll(newrentedVMlist);
		
		return newrentedVMlist;
	}
	
	/**
	 * Release the vm at the next pricing point
	 * @param vm
	 * @return
	 */
	public boolean ReleaseVM(VM vm)
	{
		for(int i=0;i<dcagentlist.size();i++)
		{
			DataCenterAgent agent=dcagentlist.get(i);
			if(agent.getVMTypes().contains(vm.vmtype))
			{
				this.existingrental+=vm.getCurrentRentalCost();
				RemoveNode(vm.nodename);
				agent.ReleaseVM(vm.nodename);
				rentedVMs.remove(vm);
				
			}
		}
		return false;
	}
	
	public double getRental()
	{
		double rental=0;
		
		for(int i=0;i<rentedVMs.size();i++)
		{
			rental+=rentedVMs.get(i).getCurrentRentalCost();
		}
		
		rental+=this.existingrental;
		Log.printLine("------------------------>Rental costs="+rental);
		return rental;
	}
	private double containercost=0;
	public double UpdateContainerCost()
	{
		containercost+=this.control_center.fibanaci_deployment.containernum*1;
		Log.printLine("------------------------>Container costs="+containercost);
		return containercost;
	}
	public double getContainerCost()
	{
		return containercost;
	}
	
	/**
	 * Add the virtual machine to the center
	 * @param nodename
	 * @return
	 */
	public boolean AddNode(String nodename)
	{
		
		PatchNodeWithLabelReplace(nodename,"nodename","rubis");
		
		return true;
		
	}
	
	/**
	 * Remove the virtual machine to the center
	 * @param nodename
	 * @return
	 */
	public boolean RemoveNode(String nodename)
	{
		PatchNodeWithLabelReplace(nodename,"nodename","no-rubis");
		DeletePod("default", nodename);
		Log.printLine("Removed:"+nodename);
		return false;
	}

	/**
	 * Path the node with given key value
	 * @param nodename
	 * @param key
	 * @param value
	 * @return
	 */
	private boolean PatchNodeWithLabelReplace(String nodename, String key, String value)
	{
		
		//CoreV1Api api = new CoreV1Api();
		CoreV1Api api = Connect2System.getAPI(); 
		//Connect2System.client.setDebugging(true);
		
		//String name = "czcworker2"; // String | name of the Node
		// 新增
		String patchstr="[{\"op\": \"replace\", \"path\": \"/metadata/labels/"+key+"\", \"value\":\""+value+"\"}]"; //rubis
		V1Patch body = new V1Patch(patchstr);
		//V1Patch body = new V1Patch("");
		// 替换更新
		//V1Patch body = new V1Patch("[{\"op\": \"replace\", \"path\": \"/metadata/labels/MixedDeploy_Status\", \"value\":\"false\"}]");
		String pretty = "true";
		String dryRun = null;
		Boolean force = null; // must be set to null for non-apply operation Boolean | Force is going to \"force\" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
		/*
		try {
			//apiInstance.listno
		    V1Node result = api.patchNode(name, body, pretty, dryRun, null, force);
		    Log.printLine(result);
		} catch (ApiException e) {
		    System.err.println("Exception when calling CoreV1Api#patchNode");
		    e.printStackTrace();
		}
		*/
		
		V1Node node;
		try {
			node = PatchUtils.patch(
			        		  V1Node.class,
			              () ->api.patchNodeCall(nodename, body, pretty, dryRun, null, force,null),
			              V1Patch.PATCH_FORMAT_JSON_PATCH,
			              api.getApiClient());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.printLine("json-patched node" + nodename);
		
		return false;
	}
	
	/**
	 * Delete pod of given namespace on the specified nodename
	 * @param namespace
	 * @param nodename
	 * @return
	 */
	private boolean DeletePod(String namespace, String nodename)
	{
		CoreV1Api coreApi = Connect2System.getAPI(); 
		//Connect2System.client.setReadTimeout(6000);
		 // String namespace="default";
		V1PodList podlist;
		try {
			podlist = coreApi.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null);
			V1Pod selectpod=null;
		    for(int i=0;i<podlist.getItems().size();i++)
		    {
		    	V1Pod pod=podlist.getItems().get(i);
		    	Log.printLine(pod.getMetadata().getName()+" "+pod.getMetadata().getNamespace()+ " "
		    	+pod.getSpec().getNodeName());
		    	if(pod.getSpec().getNodeName()!=null&&pod.getSpec().getNodeName().equalsIgnoreCase(nodename))
		    	{
		    		selectpod=pod;
		    		String podname=selectpod.getMetadata().getName();
					 // coreApi.deleteNamespacedPodAsync(podname, namespace, null, null, null, null, null, null, null);
					 //Log.printLine("try to delete "+podname);
				    Connect2System.client.setLenientOnJson(true);
				    Log.printLine("Deleted pod"+selectpod.getMetadata().getName());
				    V1Status sta;
					try {
						sta = coreApi.deleteNamespacedPod(podname, namespace, null, null, null, null, null, new V1DeleteOptions());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.printLine(e.getMessage());
						//e.printStackTrace();
					}
		    	}
		    }
			
		} catch (ApiException e1) {
			// TODO Auto-generated catch block
			Log.printLine(e1.getMessage());
		}
		
		   // ByteStreams.copy(is, System.out); 
		return true;
	}
	/**
	 * Set the pod number of an application
	 * @param namespace
	 * @param deploymentname
	 * @param containernum
	 * @return
	 */
	public boolean setContainerCount(String namespace, String deploymentname, int containernum)
	{
		// String kubeConfigPath = "D:\\workspace\\KubenetControl\\config";

		    // loading the out-of-cluster config, a kubeconfig from file-system
			
		//ApiClient	client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
				  // set the global default api-client to the in-cluster one from above
	   // Configuration.setDefaultApiClient(client);
			    
		//ExtensionsV1beta1Api api = new ExtensionsV1beta1Api(client);
		
	    
		//client.setDebugging(true);
		
		
		// 新增
		V1Patch body = new V1Patch("[{\"op\": \"replace\", \"path\": \"/spec/replicas\", \"value\":"+String.valueOf(containernum)+"}]");
		//V1Patch body = new V1Patch("");
		// 替换更新
		//V1Patch body = new V1Patch("[{\"op\": \"replace\", \"path\": \"/metadata/labels/MixedDeploy_Status\", \"value\":\"false\"}]");
	
		/*
		try {
			//apiInstance.listno
		    V1Node result = api.patchNode(name, body, pretty, dryRun, null, force);
		    Log.printLine(result);
		} catch (ApiException e) {
		    System.err.println("Exception when calling CoreV1Api#patchNode");
		    e.printStackTrace();
		}
		*/
		AppsV1Api api = new AppsV1Api(Connect2System.client);
		
		//apiInstance.patchNamespacedDeployment(name, namespace, body, pretty, dryRun, fieldManager, force)
	   // String name = "rubis-deployment"; // String | name of the Deployment
	   // String namespace = "default"; // String | object name and auth scope, such as for teams and projects
	    
	    String pretty = "true"; // String | If 'true', then the output is pretty printed.
	    String dryRun = null; // String | When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
	    String fieldManager = null; // String | fieldManager is a name associated with the actor or entity that is making these changes. The value must be less than or 128 characters long, and only contain printable characters, as defined by https://golang.org/pkg/unicode/#IsPrint. This field is required for apply requests (application/apply-patch) but optional for non-apply patch types (JsonPatch, MergePatch, StrategicMergePatch).
	    Boolean force = null; // Boolean | Force is going to \"force\" Apply requests. It means user will re-acquire conflicting fields owned by other people. Force flag must be unset for non-apply patch requests.
	   
	    try {
	    	V1Deployment result =  api.patchNamespacedDeployment(deploymentname, namespace, body, pretty, dryRun, fieldManager, force);
//	      Log.printLine(result);
	    } catch (ApiException e) {
	    	Log.printLine("Exception when calling ");
	    	Log.printLine("Status code: " + e.getCode());
	    	Log.printLine("Reason: " + e.getResponseBody());
	    	Log.printLine("Response headers: " + e.getResponseHeaders());
	      e.printStackTrace();
	      return false;
	    }
		return true;
	}
}
