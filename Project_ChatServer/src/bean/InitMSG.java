package bean;

/**
 * @author SuperSun
 * Extend MSG. Used to match the initMessage get from Server
 * IF toM is " init", get the contactList from Server
 */
public class InitMSG extends MSG{
	private ContactList contactList;

	/**
	 * Constructor
	 * @param senderId
	 * @param tOM
	 * @param contactList
	 */
	public InitMSG(String senderId, String tOM, ContactList contactList) {
		super(senderId, tOM);
		this.contactList = contactList;
	}

	public ContactList getContactList() {
		return contactList;
	}

	public void setContactList(ContactList contactList) {
		this.contactList = contactList;
	}
}
