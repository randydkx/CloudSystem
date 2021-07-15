package cn.edu.njust.utils.scheduler;

import java.util.ArrayList;

public interface FeedbackController {
	public ArrayList<Double> getState();
	public double getrequiredCapacity(double responsetime, double rk, double currentcapacity, double refprocessability, ArrayList<Double> queuelengths);
	public void setR(double r) ;
	public void updateParameters(String tracepath);
}
