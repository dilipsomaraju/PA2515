package bean;

import java.io.Serializable;

/**
 * @author SuperSun
 * The basic message template
 * IF MSG is sent by Server, sender is the message generator or null
 * IF MSG is sent by Client, sender is the logged in userId
 * 
 * toM is short for type of message, it is an important attribute
 */
public class MSG implements Serializable{
	private String senderId;
	private String tOM;

	
	public MSG() {
		super();
	}
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
