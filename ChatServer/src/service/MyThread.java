package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
	
	public void run(){
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			sendMSG(CONFIRM_MSG);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(isRun){
			try {
				msg = (MSG)(ois.readObject());
				this.getClass().getMethod(msg.gettOM()).invoke(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void login(){
		textMSG = (TextMSG)msg;
		//User DB static method
//		code like following:
//=======================================================================================
//NOTICE: Most of DB methods feedback a MSG object("","confirm")or("fail reason","error") 
//=======================================================================================
//		delete the next line when DB is ok
		MSG m = CONFIRM_MSG;
//		MSG m = LoginDB.checkLogin(textMSG.getSerderId(), textMSG.getText();
//		if(m == CONFIRM_MSG){
			//maintain userId-outputStream list
//			dto.getUserOutput().put(textMSG.getSenderId(), socket.getOutputStream());
//		}
		sendMSG(m);
	}
	
	public void logout(){
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
		String[] str = textMSG.getText().split(",");
		//DB static method to do register
//		delete the next line when DB is ok
		MSG m = CONFIRM_MSG;
//		MSG m = DB.register(textMSG.getSerderId(), str[0],str[1],str[2]);
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
