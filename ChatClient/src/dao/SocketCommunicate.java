package dao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bean.IO;
import bean.MSG;

public class SocketCommunicate implements Communicate{
	
	private IO io;
	MSG msg;
	String tOM;
	
	@Override
	public boolean connectToServer(String serverIp, int portNum) {
		try {
			Socket socket = new Socket(serverIp,portNum);
			io = new IO(new ObjectOutputStream(socket.getOutputStream()), new ObjectInputStream(socket.getInputStream()));
			msg = (MSG)(io.getOis().readObject());
			if(msg.gettOM() != null && msg.gettOM().equals("confirm"))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public MSG getMSG() {
		try {
			msg = (MSG)(io.getOis().readObject());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return msg;
	}

	public IO getIo() {
		return io;
	}

	public boolean sendMSG(MSG msg) {
		try {
			io.getOos().writeObject(msg);
			io.getOos().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
