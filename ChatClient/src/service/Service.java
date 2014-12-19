package service;

/**
 * @author SuperSun
 * Interface, provide standard functions that Client must implement
 */
public interface Service {
	/**
	 * initialize all the componets
	 */
	public void initComponents();
	
	/**
	 * connect to server
	 */
	public void connectToServer();

	/**
	 * judge whether set registerFrame visible
	 */
	public void registerFrame();

	/**
	 * register a user
	 */
	public void register();

	/**
	 * remove all the message of register frame
	 */
	public void remove();

	/**
	 * judge whether set alter password Frame visible
	 */
	public void alterPswFrame() ;
	
	/**
	 * get user' question and answer
	 */
	public void getQuestion();

	/**
	 * judge whether the answer is correct, if correct, allow user to alter password
	 */
	public void judgeQuestion();

	/**
	 * alter password
	 */
	public void alterPsw();
	
	/**
	 * transfer psw to MD5 model
	 */
	public String encryptPsw(String psw);

	/**
	 * user login
	 */
	public void login();

	/**
	 * user logout
	 */
	public void logout();
	
	/**
	 * get this user's contact list, initialize main panle
	 */
	public void initContactList();
	
	/**
	 * send message
	 */
	public void sendTextMSG();
	
	/**
	 * send group message
	 */
	public void sendGroupTextMSG();
	
	/**
	 * send file message
	 */
	public void sendFileMSG();
	
	/**
	 * send group file message
	 */
	public void sendGroupFileMSG();
	
	/**
	 * require file from server by using the link sent by server
	 */
	public void getFile();
	
	/**
	 * find user/group by id
	 */
	public void findById();
	
	/**
	 * add friend
	 */
	public void addFriend();
	
	/**
	 * rename friend
	 */
	public void renameFriend();
	
	/**
	 * delete friend
	 */
	public void deleteFriend();
	
	/**
	 * invite friends to join a group
	 */
	public void inviteToGroup();
	
	/**
	 * add group
	 */
	public void joinGroup();
	
	/**
	 * create a group
	 */
	public void createGroup();
	
	/**
	 * rename a group
	 */
	public void renameGroup();
	
	/**
	 * delete a group
	 */
	public void deleteGroup();
	
	/**
	 * delete member in a group (constaint by right)
	 */
	public void deleteGroupMember();
	
	/**
	 * get text Message from server (toM = "textMSG")
	 */
	public void textMSG();
	
	/**
	 * get group text Message from server (toM = "groupTextMSG")
	 */
	public void groupTextMSG();
	
	/**
	 * get file message from server (toM = "fileMSG")
	 */
	public void fileMSG();
	
	/**
	 * get group file message from server (toM = "groupFileMSG")
	 */
	public void groupFileMSG();
}
