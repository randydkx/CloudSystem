package cn.edu.njust.utils.publicclouds;

import cn.edu.njust.utils.kubenetecontrol.Log;
import cn.edu.njust.utils.kubenetecontrol.SystemParameter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A local cluster
 * @author czc
 *
 */
public class LocalClusterAgent implements DataCenterAgent {

	private String dataCenterName="local_center1";
	 /**
	 * The list of all available vmtypes
	 */
	private ArrayList<VMType> vmtypelist=new ArrayList<VMType>();
	
	/**
	 * All available nodes in this data center
	 * Assumped that all VMs have been added to the kubernete cluster in advance
	 */
	private HashMap<VMType, ArrayList<String>> nodemaps=new HashMap<VMType, ArrayList<String>>();
	
	/**
	 * Rented nodes
	 */
	private HashMap<String,VMType> rentednodemaps=new HashMap<String,VMType>();
	
	//private ArrayList<String> vmlist=new ArrayList<String>();
	
	
	public LocalClusterAgent() {
		super();
		//Add VM types，two types of virtual machine
		VMType type1=new VMType();
		type1.count_limit=1;
		type1.datacentername=dataCenterName;
		type1.name="i7-9750H-2.6GHZ";
		type1.priceinterval=SystemParameter.pricinginterval;
		type1.price=2;
		type1.processability=20;
		vmtypelist.add(type1);
		
		VMType type2=new VMType();
		type2.count_limit=1;
		type2.datacentername=dataCenterName;
		type2.name="i5";
		type2.priceinterval=SystemParameter.pricinginterval;
		type2.price=1;
		type2.processability=20;
		vmtypelist.add(type2);
		
		ArrayList<String> nodes=new ArrayList<String>();
//		添加对应于vmtype1的node列表，列表中保存node的名称
		nodemaps.put(type1, nodes);
		nodes.add("kube-1");
		//nodes.add("k8s-node1");
		//nodes.add("k8s-node2");
		//nodes.add("k8s-node3");
		//nodes.add("k8s-node4");
		//nodes.add("czcworker3");
		
		ArrayList<String> nodes2=new ArrayList<String>();
		nodemaps.put(type2, nodes2);
		nodes2.add("kube-1");
		//nodes2.add("czcworker2");
		//nodes2.add("czcworker4");
		
		
	}

	public HashMap<VMType, ArrayList<String>> getNodemaps() {
		return nodemaps;
	}

	@Override
	public ArrayList<String> rentVMfromDaterCenter(VMType vmtype, int num) {
		// TODO Auto-generated method stub
		ArrayList<String> rentednodes=new ArrayList<String>();
		if(nodemaps.containsKey(vmtype))
		{
			ArrayList<String> nodes=nodemaps.get(vmtype);
			for(int i=0;i<nodes.size();i++)
			{
				String tempnode=nodes.get(i);
				if(!rentednodemaps.containsKey(tempnode))//has not been rented
				{
					if(rentednodes.size()<num)
					{
						rentednodes.add(tempnode);
						rentednodemaps.put(tempnode, vmtype);
						Log.printLine("Rented:"+tempnode);
					}
					else//all nodes has been fulfilled
					{
						return rentednodes;
					}
				}
			}
		}
		
		return rentednodes;
	}

	@Override
	public ArrayList<VMType> getVMTypes() {
		// TODO Auto-generated method stub
		return vmtypelist;
	}

	@Override
	public boolean ReleaseVM(String nodename) {
		// TODO Auto-generated method stub
		if(rentednodemaps.containsKey(nodename))
		{
			rentednodemaps.remove(nodename);
			return true;
		}
		Log.printLine("ERROR:Release node without previously rented"+nodename);
		return false;
	}
	

}
