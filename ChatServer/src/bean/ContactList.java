package bean;

import java.util.List;

/**
 * @author SuperSun
 * To match the contact list both group and individule
 */
public class ContactList {
	List<Category> categories;
	List<GroupCategory> groupCategorys;
	
	
	public ContactList() {
		super();
	}
	public ContactList(List<Category> categories, List<GroupCategory> groupCategorys) {
		this.categories = categories;
		this.groupCategorys = groupCategorys;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<GroupCategory> getGroupCategorys() {
		return groupCategorys;
	}
	public void setGroupCategorys(List<GroupCategory> groups) {
		this.groupCategorys = groups;
	}
}
