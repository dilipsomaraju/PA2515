package bean;

import java.util.List;

/**
 * @author SuperSun
 * To match the contact list both group and individule
 */
public class ContactList {
	List<User> users;
	List<Group> groups;
	
	
	public ContactList() {
		super();
	}


	public ContactList(List<User> users, List<Group> groups) {
		super();
		this.users = users;
		this.groups = groups;
	}


	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}


	public List<Group> getGroups() {
		return groups;
	}


	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
}
