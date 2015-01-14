package bean;

import java.io.Serializable;

/**
 * @author SuperSun
 * To match the group contact list
 * members are users belongs to a certain group and in each User object, psw is null for security.
 */
public class Group  implements Serializable{
	private String groupId;
	private String nickName;
	private String givenName;
	
	/**
	 * Constructor
	 */
	public Group() {
	}
	
	/**
	 * Constructor
	 * @param groupId
	 * @param nickName
	 * @param givenName
	 */
	public Group(String groupId, String nickName, String givenName) {
		super();
		this.groupId = groupId;
		this.nickName = nickName;
		this.givenName = givenName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String number) {
		this.nickName = number;
	}
	public String toString(){
		return givenName+"  ("+nickName+")";
	}
}
