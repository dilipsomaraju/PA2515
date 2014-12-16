package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dao.AuthenticationDB;
import dto.Dto;

import bean.MSG;
import bean.TextMSG;

public class MyThread extends Thread{
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private Dto dto;
	private MSG msg;
	private TextMSG textMSG;
	private static MSG CONFIRM_MSG;
	private static boolean isRun = true;
	
	public MyThread(Socket socket, Dto dto) {
		this.dto = dto;
		this.socket = socket;
		CONFIRM_MSG = new MSG("","confirm");
	}
	
	public synchronized void run(){
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			sendMSG(CONFIRM_MSG);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(isRun && socket.isConnected()){
			try {
				msg = (MSG)(ois.readObject());
				if(msg != null)
					this.getClass().getMethod(msg.gettOM()).invoke(this);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
	public synchronized void login(){
		textMSG = (TextMSG)msg;
		MSG m = null;
		if(AuthenticationDB.checkLogin(textMSG.getSenderId(), textMSG.getText())){
			m = CONFIRM_MSG;
			//maintain userId-outputStream list
			dto.getUserOutput().put(textMSG.getSenderId(), oos);
		}
		else m = new MSG("UserName/Password not matched", "error");
		sendMSG(m);
	}
	
	public synchronized void logout(){
		try {
			isRun = false;
			if(msg.getSenderId()!=null){
				//TODO broadcast user loggout and change DB
			}
			ois.close();
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void register(){
		textMSG = (TextMSG)msg;
		String[] str = textMSG.getText().split("|");
		MSG m = null;
		//check if user exist
		if(AuthenticationDB.isUser(textMSG.getSenderId()))
			m = new MSG("User exist", "error");
		else {
			if(AuthenticationDB.insertUser(textMSG.getSenderId(), str[0],str[1],str[2],str[3]))
				 m = CONFIRM_MSG;
			else m = new MSG("Something wrong in DB", "error");
		}
		sendMSG(m);
	}
	
	public ObjectOutputStream getOos() {
		return oos;
	}
	private void sendMSG(MSG msg){
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
