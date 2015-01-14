package bean;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author SuperSun
 * To match the contact list both group and individule
 */
public class ContactList  implements Serializable{
	Vector<User> users;
	Vector<Group> groups;
	
	
	/**
	 * Constructor
	 */
	public ContactList() {
		super();
	}

	/**
	 * Constructor
	 * @param users user contact list
	 * @param groups group contact list
	 */
	public ContactList(Vector<User> users, Vector<Group> groups) {
		super();
		this.users = users;
		this.groups = groups;
	}

	public Vector<User> getUsers() {
		return users;
	}

	public void setUsers(Vector<User> users) {
		this.users = users;
	}

	public Vector<Group> getGroups() {
		return groups;
	}

	public void setGroups(Vector<Group> groups) {
		this.groups = groups;
	}

}
