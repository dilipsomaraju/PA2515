package bean;

import java.util.List;

public class InviteToGroupMSG extends MSG{

	private List<String> receiverIds;
	private String text;
	
	/**
	 * Constructor
	 */
	public InviteToGroupMSG() {
		super();
	}

	/**
	 * Constructor
	 * @param senderId
	 * @param tOM
	 * @param receiverIds
	 * @param text
	 */
	public InviteToGroupMSG(String senderId, String tOM, List<String> receiverIds,
			String text) {
		super(senderId, tOM);
		this.receiverIds = receiverIds;
		this.text = text;
	}
	
	public List<String> getReceiverIds() {
		return receiverIds;
	}

	public void setReceiverIds(List<String> receiverIds) {
		this.receiverIds = receiverIds;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
