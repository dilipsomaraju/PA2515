package bean;

import java.io.Serializable;


/**
 * @author SuperSun
 * Package of diskRecord.dat
 */
public class DiskData implements Serializable{
	private User user;
	private String mode;//auto login /save userId / nothing
	private String serverIp;
	private String portNum;
	
	
	public DiskData() {
		user = new User();
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getPortNum() {
		return portNum;
	}

	public void setPortNum(String portNum) {
		this.portNum = portNum;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
