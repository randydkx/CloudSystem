package cn.edu.njust.utils.scheduler;

import cn.edu.njust.utils.kubenetecontrol.Event;

public interface IDataCenterScheduler {

	public boolean Shedule(Event ev);
	public boolean PricingInterval(Event ev);
}
