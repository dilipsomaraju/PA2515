package bean;

import java.util.List;

public class GroupCategory {
	
	private String groupCategoryName;
	private int index;
	private List<Group> groups;
	
	
	public GroupCategory() {
		super();
	}
	public GroupCategory(String groupCategoryName, int index, List<Group> groups) {
		super();
		this.groupCategoryName = groupCategoryName;
		this.index = index;
		this.groups = groups;
	}
	public String getGroupCategoryName() {
		return groupCategoryName;
	}
	public void setGroupCategoryName(String groupCategoryName) {
		this.groupCategoryName = groupCategoryName;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
}
