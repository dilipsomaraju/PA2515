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
	
	void findById();
	
	void addFriend();
	
	void renameFriend();
	
	void deleteFriend();
	
	void inviteToGroup();
	
	void createGroup();
	
	void joinGroup();
	
	void renameGroup();
	
	void deleteGroup();
	
	void deleteGroupMember();
}

