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
	private static MSG ERROR_MSG;
	private static boolean isRun = true;
	
	public MyThread(Socket socket, Dto dto) {
		this.dto = dto;
		this.socket = socket;
		CONFIRM_MSG = new MSG("","confirm");
		ERROR_MSG = new MSG("","error");
	}
	
	public void run(){
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(CONFIRM_MSG);
			oos.flush();
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
//		if(DB.CheckLogin(textMSG.getSerderId(), textMSG.getText()){
			try {
				oos.writeObject(CONFIRM_MSG);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			dto.getUserOutput().put(textMSG.getSenderId(), socket.getOutputStream());//maintain userId-outputStream list
//		}
//		else{
//			oos.writeObject(ERROR_MSG);
//			oos.flush();
//		}
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

	public ObjectOutputStream getOos() {
		return oos;
	}
	
}
