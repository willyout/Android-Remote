package com.willandlily.androidrc.data;

/*
 *  The HubMachine Info Class
 */

public class Host {
	
	private String hostIP;
	private String hostPort;
	
	public Host(String hostIP, String hostPort)
	{
		this.hostIP = hostIP;
		this.hostPort = hostPort;
	} 

	public String getHostIP(){
		return this.hostIP;
	}
	
	public String getHostPort()
	{
		return this.hostPort;
	}
	
	/*
	 * http://10.75.95.217:8888/com?
	 */
	public String getURL() {
		return "http://" + hostIP + ":" + hostPort;
	}
	
	public String getCmdURL(String cmd) {
		return getURL() + "/com?" + cmd.replace(" ", "%20");
	}
	
	public String getServerURL() {
		return getURL() + "/server";
	}
}
