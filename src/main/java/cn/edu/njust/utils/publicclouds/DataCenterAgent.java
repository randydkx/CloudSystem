package cn.edu.njust.utils.publicclouds;

import java.util.ArrayList;
import java.util.HashMap;

public interface DataCenterAgent {

	/**
	 * Return the node names of the rented VM
	 * @param vmtype
	 * @param num
	 * @return
	 */
	public ArrayList<String> rentVMfromDaterCenter(VMType vmtype, int num);
	
	public ArrayList<VMType> getVMTypes();
	
	public boolean ReleaseVM(String nodename);
	public HashMap<VMType, ArrayList<String>> getNodemaps();
}
