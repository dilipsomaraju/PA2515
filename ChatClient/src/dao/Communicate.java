package dao;

import bean.MSG;


/**
 * @author SuperSun
 * An Interface to communicate with server
 */
public interface Communicate {
	
	public boolean connectToServer(String hostIp, int portNum);
	
	public MSG getMSG();
	
	public boolean sendMSG(MSG msg);
}
