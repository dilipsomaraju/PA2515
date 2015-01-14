package bean;

/**
 * @author SuperSun
 * Extend MSG. Used when send message to Server
 * text is content of message
 * receiverId is the message's destination 
 */
public class TextMSG extends MSG{
	private String receiverId;
	private String text;
	
	/**
	 * Constructor
	 */
	public TextMSG() {
		super();
	}
	
	/**
	 * Constructor
	 * @param senderId
	 * @param toM
	 * @param receiverId
	 * @param text
	 */
	public TextMSG(String senderId, String toM, String receiverId, String text) {
		super(senderId, toM);
		this.receiverId = receiverId;
		this.text = text;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
