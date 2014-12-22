package dao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bean.IO;
import bean.MSG;

/**
 * @author SuperSun
 * Implement the interface to communicate with Server
 */
public class SocketCommunicate implements Communicate{
	private IO io;
	private MSG msg;
	private Socket socket;
	
	@Override
	public boolean connectToServer(String serverIp, int portNum) {
		try {
			socket = new Socket(serverIp,portNum);
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
	
	@Override
	public MSG getMSG() {
		try {
			msg = (MSG)(io.getOis().readObject());
		} catch (Exception e) {
			//TODO to be delete
//			e.printStackTrace();
			return null;
		}
		return msg;
	}

	@Override
	public void sendMSG(MSG msg) {
		try {
			io.getOos().writeObject(msg);
			io.getOos().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return socket;
	}

	public IO getIo() {
		return io;
	}
}
