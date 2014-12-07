package bean;

import java.util.List;

public class HistoryMSG extends MSG{
	
	private List<MessageMSG> messages;

	public HistoryMSG(List<MessageMSG> messages) {
		super();
		this.messages = messages;
	}

	public List<MessageMSG> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageMSG> messages) {
		this.messages = messages;
	}
	
}
