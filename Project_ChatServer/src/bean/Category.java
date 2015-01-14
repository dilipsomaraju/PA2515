package bean;

import java.util.List;

/**
 * @author SuperSun
 * Using List<Category> to match individule contact list
 * members are users belongs to a certain category and in each User object, psw is null for security.
 */
public class Category {
	private String categoryName;
	private int index;
	private List<User> members;
	
	public Category() {
	}
	public Category(String categoryName, int index, List<User> members) {
		this.categoryName = categoryName;
		this.index = index;
		this.members = members;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<User> getMembers() {
		return members;
	}
	public void setMembers(List<User> members) {
		this.members = members;
	}
	
	
}
