package dao;

import bean.MSG;


/**
 * @author SuperSun
 * An Interface to communicate with server
 */
public interface Communicate {
	
	/**
	 * Connect to server
	 * @param hostIp server ip address
	 * @param portNum the monitoring port number of server
	 * @return if connected return true, or return false.
	 */
	public boolean connectToServer(String hostIp, int portNum);
	
	/**
	 * Get Message from server
	 * @return MSG got from server
	 */
	public MSG getMSG();
	
	/**
	 * Send a MSG message to server
	 * @param msg the message will send to server, it can be other subclasses of MSG
	 */
	public void sendMSG(MSG msg);
}
