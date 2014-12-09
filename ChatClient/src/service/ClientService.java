package service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import bean.DiskData;
import bean.MSG;
import bean.TextMSG;
import control.Control;
import dto.Dto;

public class ClientService implements Service{
	
	private Dto dto;
	
	private Control control;
	
	private JTextField serverIp;
	private JTextField portNum;
	
	private JTextField userId;
	private JPasswordField psw;
	
	private JTextField registerUserId;
	private JPasswordField registerPsw;
	private JPasswordField registerConfirmPsw;
	private JTextField question;
	private JTextField answer;
	
	private JCheckBox saveUserName;
	private JCheckBox autoLogin;
	
	private String mode;
	
	
	public ClientService(Dto dto, Control control){
		this.dto = dto;
		this.control = control;
	}

	@Override
	public void init() {
		dto.getFrameList().get("ConnectFrame").setVisible(true);
		//Init components
		serverIp = ((JTextField)(dto.getComponentList().get("serverIp")));
		portNum = ((JTextField)(dto.getComponentList().get("portNum")));
		userId = ((JTextField)(dto.getComponentList().get("userId")));
		psw = ((JPasswordField)(dto.getComponentList().get("psw")));
		saveUserName = ((JCheckBox)(dto.getComponentList().get("saveUserName")));
		autoLogin = ((JCheckBox)(dto.getComponentList().get("autoLogin")));
		registerUserId = ((JTextField)(dto.getComponentList().get("registerUserId")));
		registerPsw = ((JPasswordField)(dto.getComponentList().get("registerPassword")));
		registerConfirmPsw = ((JPasswordField)(dto.getComponentList().get("registerConfirmPassword")));
		question = ((JTextField)(dto.getComponentList().get("question")));
		answer = ((JTextField)(dto.getComponentList().get("answer")));
		
		
		serverIp.setText(dto.getServerIp());
		portNum.setText(dto.getPortNum());
		mode = dto.getMode();
		if(mode==null||mode.equals("")){
			saveUserName.setSelected(false);
			autoLogin.setSelected(false);
		}
		else if(mode.equals("saveUserName")){
			saveUserName.setSelected(true);
			autoLogin.setSelected(false);
			if(dto.getUser() != null){
				userId.setText(dto.getUser().getUserId());
			}
		}
		else if(mode.equals("autoLogin")){
			saveUserName.setSelected(true);
			saveUserName.setEnabled(false);
			autoLogin.setSelected(true);
			if(dto.getUser() != null){
				userId.setText(dto.getUser().getUserId());
				psw.setText(dto.getUser().getPsw());
			}
		}
		autoLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(autoLogin.isSelected()){
					mode = "autoLogin";
					saveUserName.setSelected(true);
					saveUserName.setEnabled(false);
				}
				else{
					saveUserName.setEnabled(true);
					mode = "saveUserName";
				}
			}
		});
		saveUserName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(saveUserName.isSelected())
					mode = "saveUserName";
				else mode = "";
				
			}
		});
	}
	@Override
	public void connectToServer() {
		serverIp = ((JTextField)(dto.getComponentList().get("serverIp")));
		portNum = ((JTextField)(dto.getComponentList().get("portNum")));
		if(serverIp.getText().equals(""))
			JOptionPane.showMessageDialog(null,
					"Please input Server IP", "Warming", JOptionPane.WARNING_MESSAGE);
		else if(portNum.getText().equals(""))
			JOptionPane.showMessageDialog(null, 
					"Please input port number", "Warming", JOptionPane.WARNING_MESSAGE);
		//If feedback is "CONFIRM_MSG", show LoginFrame
		else {
			if(dto.getSc().connectToServer(serverIp.getText(),Integer.parseInt(portNum.getText()))){
				//Set oos & ois into dto
				dto.setOis(dto.getSc().getIo().getOis());
				dto.setOos(dto.getSc().getIo().getOos());
				dto.getFrameList().get("ConnectFrame").setVisible(false);
				dto.getFrameList().get("LoginFrame").setVisible(true);
				if(autoLogin.isSelected()){
					login();
				}
			}
			else JOptionPane.showMessageDialog(null,
					"Failed to connect to Server", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	@Override
	public synchronized void register() {
		if(registerUserId.getText().equals(""))
			JOptionPane.showMessageDialog(null,
					"Please input your id", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(!registerUserId.getText().matches("\\d+"))
			JOptionPane.showMessageDialog(null,
					"Id should be INTEGER", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(registerUserId.getText().length() > 9)
			JOptionPane.showMessageDialog(null,
					"The length of Id should be at most 9 bits", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(new String(registerPsw.getPassword()).equals(""))
			JOptionPane.showMessageDialog(null,
					"Please input your password", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(new String(registerConfirmPsw.getPassword()).equals(""))
			JOptionPane.showMessageDialog(null,
					"Please confirme your password", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(!new String(registerConfirmPsw.getPassword()).equals(new String(registerPsw.getPassword())))
			JOptionPane.showMessageDialog(null,
					"The two passwords are not the same", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(question.getText().equals(""))
			JOptionPane.showMessageDialog(null,
					"Please input your question", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(answer.getText().equals(""))
			JOptionPane.showMessageDialog(null,
					"Please input your answer", "Warning", JOptionPane.WARNING_MESSAGE);
		else{
			//Send register MSG
			dto.getSc().sendMSG(new TextMSG(registerUserId.getText(),"register","",
					encryptPsw(new String(registerPsw.getPassword()))+","+question.getText()+","+answer.getText()));
			MSG m = dto.getSc().getMSG();
			if(m.gettOM().equals("confirm")){
				JOptionPane.showMessageDialog(null,
						"Successfully registered", "Information", JOptionPane.INFORMATION_MESSAGE);
				dto.getFrameList().get("RegisterFrame").setVisible(false);
			}
			else JOptionPane.showMessageDialog(null,
					m.getSenderId(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void remove(){
		registerUserId.setText("");
		registerPsw.setText("");
		registerConfirmPsw.setText("");
		question.setText("");
		answer.setText("");
	}
	
	public void registerFrame() {
		dto.getFrameList().get("RegisterFrame").setVisible(true);
		
	}
	
	public void alterPswFrame(){
		dto.getFrameList().get("AlterPswFrame").setVisible(true);
	}

	public synchronized void alterPsw(){
		//TODO
	}
	
	public void getQuestion(){
		//TODO 
		System.out.println("getQuestion");
	}
	
	public synchronized void forgetPassword(){
		//TODO 
		System.out.println("forgetPassword");
	}
	
	public void findPswFrame(){
		if(userId.getText().equals("")){
			JOptionPane.showMessageDialog(null,
					"Please input your id", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		getQuestion();
		dto.getFrameList().get("FindPswFrame").setVisible(true);
	}

	@Override
	public void login() {
		if(userId.getText().equals("")){
			JOptionPane.showMessageDialog(null,
					"Please input your id", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		else if(new String(psw.getPassword()).equals("")){
			JOptionPane.showMessageDialog(null,
					"Please input your password", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
			dto.getSc().sendMSG(new TextMSG(userId.getText(),
					"login",null,encryptPsw(new String(psw.getPassword()))));
			MSG m =  dto.getSc().getMSG();
			if(m.gettOM().equals("confirm")){
				dto.getFrameList().get("LoginFrame").setVisible(false);
				dto.getFrameList().get("MainFrame").setVisible(true);
				DiskData dd = new DiskData();
				dd.setMode(mode);
				dd.setUserId(userId.getText());
				if(!autoLogin.isSelected())
					dd.setPortNum("");
				dd.setPsw(new String(psw.getPassword()));
				dd.setPortNum(portNum.getText());
				dd.setServerIp(serverIp.getText());
				control.getDiskData().saveData(dd);
				//Strat a listener Thread
				new ListenerTread();
			}
			else JOptionPane.showMessageDialog(null,
					m.getSenderId(), "ERROR", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public String encryptPsw(String str) {
		String s = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");  
				byte[] bytes = md5.digest(str.getBytes());  
				StringBuffer stringBuffer = new StringBuffer();  
				for (byte b : bytes){  
					int bt = b&0xff;  
					if (bt < 16){  
						stringBuffer.append(0);  
					}   
				stringBuffer.append(Integer.toHexString(bt));  
				}
				s = stringBuffer.toString();  
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();  
		}
		return s;
	}
	public void logout() {
		// When has Oos but user didn't logged in 
		if(dto.getOos() != null&&dto.getUser() == null){
			dto.getSc().sendMSG(new MSG(null,"logout"));
		}
		// When user logged in
		else if(dto.getOos() != null&&dto.getUser() != null){
			dto.getSc().sendMSG(new MSG(dto.getUser().getUserId(),"logout"));
		}
	}

	@Override
	public void sendTextMSG() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendGroupTextMSG() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFileMSG() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendGroupFileMSG() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void getFile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void checkChatHistory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void addCategory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void deleteCategory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void renameCategory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void changeCategoryIndex() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void findById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void addFriend() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void renameFriend() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void deleteFriend() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void relocateFriend() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void inviteToGroup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void addGroupCategory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void renameGroupCategory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void deleteGroupCategory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void changeGroupCategoryIndex() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void addGroup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameGroup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void relocateGroup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void deleteGroup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void deleteGroupMember() {
		// TODO Auto-generated method stub
		
	}
	private class ListenerTread extends Thread{
		public synchronized void run(){
			
		}
	}
	//Unused methods
	public void saveUserName() {}
	public void autoLogin() {}
}
