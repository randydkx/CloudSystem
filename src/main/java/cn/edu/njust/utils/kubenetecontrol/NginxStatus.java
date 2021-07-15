package cn.edu.njust.utils.kubenetecontrol;

import java.util.Date;

/*
 * Active connections: 2 
server accepts handled requests
 134 134 134 
Reading: 0 Writing: 2 Waiting: 0 
 */
public class NginxStatus {

	public double Active_connections;
	public double accepts_connections;
	public double handled_connections;
	public double handled_requests;
	public double readingandwriting;
	public double waiting;
	public Date date;
}
