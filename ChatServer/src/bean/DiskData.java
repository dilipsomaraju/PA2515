package bean;

import java.io.Serializable;


public class DiskData implements Serializable{
	
	private User user;
	private ContactList contactList;
	private String mode;//auto login /save userId / nothing 
	private String sendMsgMode; //press "Enter"/"Ctrl + Enter" to send message
	private String serverIp;
	private String portNum;
	
	
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

	public String getSendMsgMode() {
		return sendMsgMode;
	}

	public void setSendMsgMode(String sendMsgMode) {
		this.sendMsgMode = sendMsgMode;
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
	public ContactList getContactList() {
		return contactList;
	}
	public void setContactList(ContactList cl) {
		this.contactList = cl;
	}
	
	
}
