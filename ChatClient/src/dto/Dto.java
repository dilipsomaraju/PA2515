package dto;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import bean.ContactList;
import bean.DiskData;
import bean.User;
import dao.SocketCommunicate;

/**
 * @author SuperSun
 * Data translate object, it plays the role as Data source, contains:
 * logged in user info
 * current msg (need syncronized lock the operation)
 * chatFrameList is a map<speaker id, Frame object>
 * cl is the ContactList
 */
public class Dto {
	private User user;
	private String receiverId;
	private HashMap<String, JFrame> frameList;
	private HashMap<String, JPanel> chatPanelList;
	private HashMap<String, JComponent> componentList;
	private ContactList contactList;
	private SocketCommunicate sc;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String mode;//auto login /save userId / "" 
	private boolean isRun;
	private String serverIp;
	private String portNum;
	
	public Dto() {
		user = new User();
		contactList = new ContactList();
		frameList = new HashMap<String, JFrame>();
		chatPanelList = new HashMap<String, JPanel>();
		componentList = new HashMap<String, JComponent>();
		isRun = true;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public HashMap<String, JFrame> getFrameList() {
		return frameList;
	}

	public void setFrameList(HashMap<String, JFrame> frameList) {
		this.frameList = frameList;
	}

	public HashMap<String, JPanel> getChatPanelList() {
		return chatPanelList;
	}

	public void setChatPanelList(HashMap<String, JPanel> chatPanelList) {
		this.chatPanelList = chatPanelList;
	}

	public HashMap<String, JComponent> getComponentList() {
		return componentList;
	}

	public void setComponentList(HashMap<String, JComponent> componentList) {
		this.componentList = componentList;
	}

	public ContactList getContactList() {
		return contactList;
	}

	public void setContactList(ContactList contactList) {
		this.contactList = contactList;
	}

	public SocketCommunicate getSc() {
		return sc;
	}

	public void setSc(SocketCommunicate sc) {
		this.sc = sc;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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

	public void setDiskRecord(DiskData diskData) {
		this.user.setUserId(diskData.getUserId());
		this.user.setPsw(diskData.getPsw());
		this.mode = diskData.getMode();
		this.serverIp = diskData.getServerIp();
		this.portNum = diskData.getPortNum();
	}
}
