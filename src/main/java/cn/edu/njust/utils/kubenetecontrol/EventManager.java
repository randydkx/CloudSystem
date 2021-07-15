package cn.edu.njust.utils.kubenetecontrol;

import java.util.ArrayList;
import java.util.Date;


public class EventManager {

	private ArrayList<Event> event_queue=new ArrayList<Event>();// the queue to store events
	
	/**
	 * Get the next sleep time
	 * @return
	 */
	public long getNextSleepTime()
	{
		if(event_queue.size()<0)
			return -1;
		Event ev1=event_queue.get(0);
		if(ev1!=null)
		{
			Date nowtime=new Date();
			//int gaps=nowtime.compareTo(ev1.time);
			long gaps=ev1.time.getTime()-nowtime.getTime();//milliseconds
			return gaps;
		}
		return -1;
	}
	
	/**
	 * Add events to this priority queue
	 * @param event
	 */
	public void addEvent(Event event)
	{
		//System.out.println("///////////////before");
		for(int i=0;i<event_queue.size();i++)
		{
			Event oldevent=event_queue.get(i);
			//System.out.println("position:"+i+oldevent.time);
		}
		//System.out.println("///////////////");
		//event_queue.add(event);
		boolean added=false;
		for(int i=0;i<event_queue.size();i++)
		{
			Event oldevent=event_queue.get(i);
			if(event.time.getTime()<oldevent.time.getTime())
			{
				event_queue.add(i, event);
				//System.out.println("Added event:"+event.time+"-"+event.tag);
				added=true;
				break;
			}
		}
		if(!added)
		{
			//System.out.println("Added event:"+event.time+"-"+event.tag);
			event_queue.add(event);
		}
		//System.out.println("/////after");
		
		for(int i=0;i<event_queue.size();i++)
		{
			Event oldevent=event_queue.get(i);
			//System.out.println("position:"+i+oldevent.time);
		}
		//System.out.println("///////////////");
		
	}
	
	/**
	 * get all earliest events with equal times
	 * @return
	 */
	public ArrayList<Event> getNextEvents()
	{
		ArrayList<Event> nextevents_equaltimes=new ArrayList<Event>();
		
		if(event_queue.size()>0)
		{
			Event ev1=event_queue.remove(0);
			nextevents_equaltimes.add(ev1);
			
			while(event_queue.size()>0)
			{
				Event ev2=event_queue.get(0);
				if(ev1.time.equals(ev2.time))
				{
					event_queue.remove(0);
					nextevents_equaltimes.add(ev2);
				}
				else
				{
					break;
				}
				
			}
		}
		
		return nextevents_equaltimes;
	}
	/*
	 * class UserComparator implements Comparator<Event> { public int compare(Event
	 * e1, Event e2) { long t1=e1.time.getTime(); long t2=e2.time.getTime(); long
	 * gap=t1-t2; if (gap<0) { return -1; } else if (gap==0) { return 0; } else {
	 * return 1; } } }
	 */
}
