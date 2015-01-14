package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import dto.Dto;


public class Service implements Runnable{
	private static int SERVER_PORT;
	private ServerSocket serverSocket;
	private Socket socket;
	private Dto dto;
	
	public Service(Dto dto) {
		this.dto = dto;
	}

	//Listend for Clients' Connection
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			while(true){
				socket = serverSocket.accept();
				new MyThread(socket,dto).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startServer() {
		while(SERVER_PORT == 0){
			getPort();
		}
		new Thread(this).start();
	}

	public void getPort(){
		//get initial port number
		String  temp = JOptionPane.showInputDialog("Please input the port number:");
		//click cancle or close window 
		if(temp == null) {
			int i = JOptionPane.showConfirmDialog(null,
					"Do you want to close?", "Warming",
					JOptionPane.YES_NO_OPTION);
			if(Math.abs(i) == 1)
				return;
			System.exit(0);
		}
		//portNum < 0 or not integer
		if(!temp.matches("\\d+")){
			JOptionPane.showMessageDialog(null,
					"Need a port number range 1024 to 65535!",
						"Wrong Input!",JOptionPane.ERROR_MESSAGE);
			return;
		}
		//cast to int
		int port = Integer.parseInt(temp);
		//portNum belongs to (1024, 65535)
		if(port < 1024 || port > 65535){
			JOptionPane.showMessageDialog(null,
					"Need a port number range 1024 to 65535!",
						"Wrong Input!",JOptionPane.ERROR_MESSAGE);
			return;
		}
		SERVER_PORT = port;
	}

	
}
