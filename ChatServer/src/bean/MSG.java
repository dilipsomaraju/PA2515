package bean;

import java.io.Serializable;

/**
 * @author SuperSun
 * The basic message template
 */
public class MSG implements Serializable{
	private String senderId;
	private String tOM;

	
	/**
	 * Constructor
	 */
	public MSG() {
		super();
	}
	
	/**
	 * Constructor
	 * @param senderId
	 * @param tOM
	 */
	public MSG(String senderId, String tOM) {
		this.senderId = senderId;
		this.tOM = tOM;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String speakerId) {
		this.senderId = speakerId;
	}
	public String gettOM() {
		return tOM;
	}
	public void settOM(String typeOfMSG) {
		this.tOM = typeOfMSG;
	}
}
