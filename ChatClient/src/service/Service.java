package service;

/**
 * @author SuperSun
 * Interface, provide standard functions that Client must implement
 */
public interface Service {
	
	void init();
	
	void connectToServer();
	
	void register();
	
	void login();
	
	/**
	 * transfer psw to MD5 model
	 */
	String encryptPsw(String psw);
	
	void logout();
	
	void sendTextMSG();
	
	void sendGroupTextMSG();
	
	void sendFileMSG();
	
	void sendGroupFileMSG();
	
	void getFile();
	
	void checkChatHistory();
	
	void addCategory();
	
	void deleteCategory();
	
	void renameCategory();
	
	void changeCategoryIndex();
	
	void findById();
	
	void addFriend();
	
	void renameFriend();
	
	void deleteFriend();
	
	void relocateFriend();
	
	void inviteToGroup();
	
	void addGroupCategory();
	
	void renameGroupCategory();
	
	void deleteGroupCategory();
	
	void changeGroupCategoryIndex();
	
	void addGroup();
	
	void renameGroup();
	
	void relocateGroup();
	
	void deleteGroup();
	
	void deleteGroupMember();
}

