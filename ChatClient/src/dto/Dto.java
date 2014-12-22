package dto;

import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import bean.ContactList;
import bean.DiskData;
import control.Control;
import dao.SocketCommunicate;

/**
 * @author SuperSun
 * Data translate object
 */
public class Dto {
	private String receiverId;
	private HashMap<String, JFrame> frameList;
	private HashMap<String, JPanel> chatPanelList;
	private HashMap<String, JComponent> componentList;
	private ContactList contactList;
	private SocketCommunicate sc;
	private DiskData diskData;
	private boolean isRun;
	private Control control;
	
	/**
	 * Constructor
	 */
	public Dto(Control control) {
		contactList = new ContactList();
		frameList = new HashMap<String, JFrame>();
		chatPanelList = new HashMap<String, JPanel>();
		componentList = new HashMap<String, JComponent>();
		this.control = control;
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

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	
	public DiskData getDiskData(){
		return diskData;
	}
	public void setDiskData(DiskData diskData) {
		this.diskData = diskData;
	}

	public Control getControl() {
		return control;
	}
}
