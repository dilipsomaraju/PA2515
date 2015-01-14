package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import util.Time;
import bean.ContactList;
import bean.FileMSG;
import bean.InitMSG;
import bean.InviteToGroupMSG;
import bean.MSG;
import bean.MessageMSG;
import bean.TextMSG;
import bean.User;
import dao.AuthenticationDB;
import dao.ChatDB;
import dao.ContactListDB;
import dto.Dto;

public class MyThread extends Thread{
	/**
	 * MSG get from Client
	 */
	private MSG msg;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private Dto dto;
	private static MSG CONFIRM_MSG;
	private boolean isRun = true;
	private String userId;
	
	public MyThread(Socket socket, Dto dto) {
		this.dto = dto;
		this.socket = socket;
		CONFIRM_MSG = new MSG("","confirm");
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			sendMSG(CONFIRM_MSG);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void run(){
		while(isRun && socket.isConnected()){
			try {
				msg = (MSG)(ois.readObject());
				if(msg != null)
					this.getClass().getMethod(msg.gettOM()).invoke(this);
				sleep(100);
			} catch (Exception e) {
				dto.getUserOutput().remove(userId);
				AuthenticationDB.setStatus(userId, 0);
				try {
					socket.shutdownInput();
					socket.shutdownOutput();
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				break;
			}
		}
		AuthenticationDB.setStatus(userId, 0);
	}
	
	public synchronized void login(){
		TextMSG textMSG = (TextMSG)msg;
		userId = textMSG.getSenderId();
		MSG m = null;
		if(AuthenticationDB.checkLogin(userId, textMSG.getText())){
			m = CONFIRM_MSG;
			//maintain userId-outputStream list
			dto.getUserOutput().put(userId, oos);
			sendMSG(m);
			List<String> userIds = ChatDB.getLoggedFriendNames(userId);
			TextMSG tmsg = new TextMSG(userId,"broadcast",null,"login");
			for(String uId : userIds){
				try {
					dto.getUserOutput().get(uId).writeObject(tmsg);
					dto.getUserOutput().get(uId).flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			m = new MSG("UserName/Password not matched\n" +
					"OR This account has logged", "error");
			sendMSG(m);
		}
	}
	
	public synchronized void refreshContactList(){
		ContactList cl = ContactListDB.getContactList(userId);
		//send contact list
		if(cl != null)
			sendMSG(new InitMSG("confirm","refreshContactList", cl));
		else 
			sendMSG(new InitMSG("Something wrong in DB","error", null));
	}
	
	public synchronized void logout(){
		try {
			isRun = false;
			run();
			if(userId != null){
				AuthenticationDB.setStatus(userId, 0);
				List<String> userIds = ChatDB.getLoggedFriendNames(userId);
				TextMSG tmsg = new TextMSG(userId,"broadcast",null,"logout");
				for(String uId : userIds){
					try {
						dto.getUserOutput().get(uId).writeObject(tmsg);
						dto.getUserOutput().get(uId).flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				dto.getUserOutput().remove(userId);
			}
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void register(){
		TextMSG textMSG = (TextMSG)msg;
		String[] str = textMSG.getText().split("<>");
		MSG m = null;
		if(AuthenticationDB.insertUser(textMSG.getSenderId(), str[0],str[1],str[2],str[3]))
			 m = CONFIRM_MSG;
		else m = new MSG("Something wrong in DB\nNotice: Only English is allowed", "error");
		sendMSG(m);
	}
	
	public synchronized void isUser(){
		MSG m = null;
		if(AuthenticationDB.isUser(msg.getSenderId()))
			m = CONFIRM_MSG;
		else m = new MSG(null,"error");
		sendMSG(m);
	}
	
	public synchronized void getQuestion(){
		TextMSG m = null;
		User user = AuthenticationDB.getUser(msg.getSenderId());
		m = new TextMSG(null, "confirm",user.getQuestion(),user.getAnswer());
		sendMSG(m);
	}
	
	public synchronized void alterPsw(){
		TextMSG textMSG = (TextMSG)msg;
		MSG m = null;
		if(AuthenticationDB.alterPsw(textMSG.getSenderId(), textMSG.getText()))
			m = CONFIRM_MSG;
		else m = new MSG("Something wrong in accessing Database","error");
		sendMSG(m);
	}

	public synchronized void find() {
		MSG m = null;
		TextMSG textMSG = (TextMSG)msg;
		String tableName = textMSG.getSenderId();
		String attibute = textMSG.getReceiverId();
		String clue = textMSG.getText();
		Vector<String> ids = ContactListDB.find(tableName, attibute, clue);
		if(ids == null)
			m = new InviteToGroupMSG("Something wrong in accessing Database","changeFindList",null,null);
		else{
			m = new InviteToGroupMSG("confirm", "changeFindList", ids, null);
		}
		sendMSG(m);
	}
	
	public synchronized void addFriend(){
		TextMSG textMSG = (TextMSG)msg;
		MSG m = null;
		if(ContactListDB.hasThisFriend(userId, textMSG.getSenderId())){
			m = new MSG("You have already added this user/group","addFriend");
			sendMSG(m);
			return;
		}
		if(ContactListDB.addFriend(userId,textMSG.getSenderId(),textMSG.getText()))
			m = new MSG(textMSG.getSenderId(),"addFriend");
		else m = new MSG("Something wrong in accessing Database","addFriend");
		sendMSG(m);
	}
	
	public synchronized void renameFriend() {
		TextMSG textMSG = (TextMSG)msg;
		TextMSG m = null;
		if(ContactListDB.reNameFriend(userId, textMSG.getSenderId(), textMSG.getText()))
			m = new TextMSG(textMSG.getSenderId(),"renameFriend",null,textMSG.getText());
		else m = new TextMSG("Something wrong in accessing Database","renameFriend",null,null);
		sendMSG(m);
	}
	
	public synchronized void deleteFriend() {
		MSG m = null;
		if(ContactListDB.deleteFirend(userId, msg.getSenderId()))
			m = new MSG(msg.getSenderId(),"deleteFriend");
		else m = new MSG("Something wrong in accessing Database","deleteFriend");
		sendMSG(m);
	}
	
	public synchronized void inviteToGroup() {
		// TODO LAST to implement
		
	}
	
	public synchronized void isGroup(){
		MSG m = null;
		if(ContactListDB.isGroup(msg.getSenderId()))
			m = new MSG("This group id is exist","isGroup");
		else m = new MSG(msg.getSenderId(),"isGroup");
		sendMSG(m);
	}
	
	public synchronized void createGroup() {
		TextMSG textMSG = (TextMSG)msg;
		MSG m = null;
		if(ContactListDB.createGroup(textMSG.getSenderId(),textMSG.getReceiverId(),userId,textMSG.getText()))
			m = new MSG("confirm","createGroup");
		else m = new MSG("Something wrong in accessing Database","createGroup");
		sendMSG(m);
	}
	
	public synchronized void addGroup() {
		TextMSG textMSG = (TextMSG)msg;
		MSG m = null;
		if(ContactListDB.hashThisGroup(userId, textMSG.getSenderId())){
			m = new MSG("You have already added this user/group","addGroup");
			sendMSG(m);
			return;
		}
		if(ContactListDB.addGroup(textMSG.getSenderId(),userId,textMSG.getText()))
			m = new MSG(textMSG.getSenderId(),"addGroup");
		else m = new MSG("Something wrong in accessing Database","addGroup");
		sendMSG(m);
	}
	
	public synchronized void renameGroup() {
		TextMSG textMSG = (TextMSG)msg;
		TextMSG m = null;
		if(ContactListDB.reNameGroup(textMSG.getSenderId(), userId, textMSG.getText()))
			m = new TextMSG("g"+textMSG.getSenderId(),"renameGroup",null,"G:"+textMSG.getText());
		else m = new TextMSG("Something wrong in accessing Database","renameGroup",null,null);
		sendMSG(m);
	}
	
	public synchronized void deleteGroup() {
		MSG m = null;
		if(ContactListDB.deleteGroup(msg.getSenderId(), userId))
			m = new MSG("g"+msg.getSenderId(),"deleteGroup");
		else m = new MSG("Something wrong in accessing Database","deleteGroup");
		sendMSG(m);
	}
	
	public synchronized void deleteGroupMember() {
		// TODO LAST LAST TO Implement
		
	}
	
	public synchronized void sendTextMSG() {
		TextMSG textMSG = (TextMSG)msg;
		String senderId = textMSG.getSenderId();
		String userNickName = AuthenticationDB.getUserNickName(senderId);
		String receiverId = textMSG.getReceiverId();
		String text = textMSG.getText();
		String time = Time.getTime();
		//Send to client itself
		MessageMSG mmsg = new MessageMSG(senderId, "textMSG", receiverId, text, null, time,userNickName);
		sendMSG(mmsg);
		//send to receiver
		try {
			dto.getUserOutput().get(receiverId).writeObject(mmsg);
			dto.getUserOutput().get(receiverId).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sendGroupTextMSG() {
		TextMSG textMSG = (TextMSG)msg;
		String senderId = textMSG.getSenderId();
		String userNickName = AuthenticationDB.getUserNickName(senderId);
		String groupId = textMSG.getReceiverId();
		String text = textMSG.getText();
		String time = Time.getTime();
		MessageMSG mmsg = new MessageMSG(senderId, "groupTextMSG", groupId, text, null, time,userNickName);
		//send to group members(including this client)
		List<String> userIds = ChatDB.getLoggedGroupMemberNames(groupId);
		try {
			for(String userId : userIds){
				dto.getUserOutput().get(userId).writeObject(mmsg);
				dto.getUserOutput().get(userId).flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sendFileMSG() {
		FileMSG fileMSG = (FileMSG)msg;
		String senderId = fileMSG.getSenderId();
		String userNickName = AuthenticationDB.getUserNickName(senderId);
		String receiverId = fileMSG.getReceiverId();
		File file = fileMSG.getFile();
		String fileName = file.getName();
		String time = Time.getTime();
		String filePath = "file\\"+senderId+Time.getTime(time)+".dat";
		try {
			File f = new File(filePath);
			if(f.createNewFile()){
				ObjectOutputStream os =  new ObjectOutputStream(new FileOutputStream(filePath));
				os.writeObject(file);
				os.flush();
				os.close();
			}
		} catch (IOException e) {
		e.printStackTrace();
		}
		MessageMSG mmsg = new MessageMSG(senderId,"fileMSG",receiverId,fileName,filePath,time,userNickName);
		sendMSG(mmsg);
		try {
			dto.getUserOutput().get(receiverId).writeObject(mmsg);
			dto.getUserOutput().get(receiverId).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sendGroupFileMSG() {
		FileMSG fileMSG = (FileMSG)msg;
		String senderId = fileMSG.getSenderId();
		String userNickName = AuthenticationDB.getUserNickName(senderId);
		String groupId = fileMSG.getReceiverId();
		File file = fileMSG.getFile();
		String fileName = file.getName();
		String time = Time.getTime();
		String filePath = "file\\"+senderId+Time.getTime(time)+".dat";
		try {
			File f = new File(filePath);
			if(f.createNewFile()){
				ObjectOutputStream os =  new ObjectOutputStream(new FileOutputStream(filePath));
				os.writeObject(file);
				os.flush();
				os.close();
			}
		} catch (IOException e) {
		e.printStackTrace();
		}
		MessageMSG mmsg = new MessageMSG(senderId,"groupFileMSG",groupId,fileName,filePath,time,userNickName);
		try {
			List<String> userIds = ChatDB.getLoggedGroupMemberNames(groupId);
			for(String userId : userIds){
				dto.getUserOutput().get(userId).writeObject(mmsg);
				dto.getUserOutput().get(userId).flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void getFile() {
		String filePath = msg.getSenderId();
		File file = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
				file = (File) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileMSG fmsg = new FileMSG(null,"getFile",null,file);
		sendMSG(fmsg);
	}
	
	private synchronized void sendMSG(MSG msg){
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
