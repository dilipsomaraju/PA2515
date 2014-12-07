package bean;

import java.util.List;

/**
 * @author SuperSun
 * To match the group contact list
 * members are users belongs to a certain group and in each User object, psw is null for security.
 */
public class Group {
	private String name;
	private String nickname;
	private int right;
	private List<User> members;
	
	public Group() {
	}
	
	public Group(String name, String nickname, int right, List<User> members) {
		super();
		this.name = name;
		this.nickname = nickname;
		this.right = right;
		this.members = members;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public String getName() {
		return name;
	}
	public void setName(String number) {
		this.name = number;
	}
	public List<User> getMembers() {
		return members;
	}
	public void setMembers(List<User> members) {
		this.members = members;
	}
	
}
