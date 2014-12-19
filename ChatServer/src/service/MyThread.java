package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bean.InitMSG;
import bean.MSG;
import bean.MessageMSG;
import bean.TextMSG;
import bean.User;
import dao.AuthenticationDB;
import dto.Dto;

public class MyThread extends Thread{
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private Dto dto;
	private MSG msg;
	private TextMSG textMSG;
	private MessageMSG mMSG;
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
	}
	
	public synchronized void login(){
		textMSG = (TextMSG)msg;
		userId = textMSG.getSenderId();
		MSG m = null;
		if(AuthenticationDB.checkLogin(userId, textMSG.getText())){
			m = CONFIRM_MSG;
			//maintain userId-outputStream list
			dto.getUserOutput().put(userId, oos);
		}
		else m = new MSG("UserName/Password not matched", "error");
		sendMSG(m);
		//send contact list
		sendMSG(new InitMSG(null,"initContactList",AuthenticationDB.getContactList(userId)));
	}
	
	public synchronized void logout(){
		try {
			isRun = false;
			run();
			if(userId != null){
				//TODO broadcast user loggout and change DB
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
		textMSG = (TextMSG)msg;
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
		textMSG = (TextMSG)msg;
		MSG m = null;
		if(AuthenticationDB.alterPsw(textMSG.getSenderId(), textMSG.getText()))
			m = CONFIRM_MSG;
		else m = new MSG("Something wrong in accessing Database","error");
		sendMSG(m);
	}

	public synchronized void findById() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void addFriend(){
		//TODO Auto-generated method stub
	}
	
	public synchronized void renameFriend() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void deleteFriend() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void inviteToGroup() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void createGroup() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void joinGroup() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void renameGroup() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void deleteGroup() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void deleteGroupMember() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void sendTextMSG() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void sendGroupTextMSG() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void sendFileMSG() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void sendGroupFileMSG() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void getFile() {
		// TODO Auto-generated method stub
		
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
