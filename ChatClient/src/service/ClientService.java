package service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ui.Img;
import ui.MainFrame;
import bean.DiskData;
import bean.InitMSG;
import bean.MSG;
import bean.TextMSG;
import control.Control;
import dto.Dto;

public class ClientService implements Service{
	private Dto dto;
	private Control control;
	//connect parameter
	private JTextField serverIp;
	private JTextField portNum;
	//login parameter
	private JTextField userId;
	private JPasswordField psw;
	private JCheckBox saveUserName;
	private JCheckBox autoLogin;
	private String mode;
	//register parameter
	private JTextField registerUserName;
	private JPasswordField registerPsw;
	private JPasswordField registerConfirmPsw;
	private JTextField question;
	private JTextField answer;
	//alter password parameter
	private JTextField alterPswQuestion;
	private JTextField alterPswAnswer;
	private JPasswordField alterPsw;
	private JPasswordField confirmAlterPsw;
	private String answerString;
	//msg got from server
	private MSG msg;

//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ClientService(Dto dto, Control control){
		this.dto = dto;
		this.control = control;
	}

	@Override
	public void initComponents() {
		dto.getFrameList().get("ConnectFrame").setVisible(true);
		//ConnectToServer components
		serverIp = ((JTextField)(dto.getComponentList().get("serverIp")));
		portNum = ((JTextField)(dto.getComponentList().get("portNum")));
		//Login components
		userId = ((JTextField)(dto.getComponentList().get("userId")));
		psw = ((JPasswordField)(dto.getComponentList().get("psw")));
		saveUserName = ((JCheckBox)(dto.getComponentList().get("saveUserName")));
		autoLogin = ((JCheckBox)(dto.getComponentList().get("autoLogin")));
		//Register components
		registerUserName = ((JTextField)(dto.getComponentList().get("registerUserName")));
		registerPsw = ((JPasswordField)(dto.getComponentList().get("registerPassword")));
		registerConfirmPsw = ((JPasswordField)(dto.getComponentList().get("registerConfirmPassword")));
		question = ((JTextField)(dto.getComponentList().get("question")));
		answer = ((JTextField)(dto.getComponentList().get("answer")));
		//Alter Password components
		alterPswQuestion = ((JTextField)(dto.getComponentList().get("alterPswQuestion")));
		alterPswAnswer = ((JTextField)(dto.getComponentList().get("alterPswAnswer")));
		alterPsw = ((JPasswordField)(dto.getComponentList().get("alterrPassword")));
		confirmAlterPsw = ((JPasswordField)(dto.getComponentList().get("confirmAlterPassword")));
		//Setting
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
		
		serverIp.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					connectToServer();
			}
		});
		
		portNum.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					connectToServer();
			}
		});
		
		userId.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					login();
			}
		});
		
		psw.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					login();
			}
		});
		
		autoLogin.addActionListener(new ActionListener() {
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
			public void actionPerformed(ActionEvent e) {
				if(saveUserName.isSelected())
					mode = "saveUserName";
				else mode = "";
			}
		});
	}
	
	@Override
	public synchronized void connectToServer() {
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
	public synchronized void registerFrame() {
		if(userId.getText().equals(""))
			JOptionPane.showMessageDialog(null,
					"Please input your id", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(!userId.getText().matches("\\d+"))
			JOptionPane.showMessageDialog(null,
					"Id should be INTEGER", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(userId.getText().length() > 9)
			JOptionPane.showMessageDialog(null,
					"The length of Id should be at most 9 bits", "Warning", JOptionPane.WARNING_MESSAGE);
		else{
			dto.getSc().sendMSG(new MSG(userId.getText(),"isUser"));
			MSG m = dto.getSc().getMSG();
			if(m.gettOM().equals("confirm")){
				JOptionPane.showMessageDialog(null,
						"User exist", "Error", JOptionPane.ERROR_MESSAGE);
				dto.getFrameList().get("RegisterFrame").setVisible(false);
			}
			else dto.getFrameList().get("RegisterFrame").setVisible(true);
		}
	}
	
	@Override
	public synchronized void register() {
		if(registerUserName.getText().equals(""))
			JOptionPane.showMessageDialog(null,
					"Please input your Nickname", "Warning", JOptionPane.WARNING_MESSAGE);
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
			dto.getSc().sendMSG(new TextMSG(userId.getText(),"register","",
					encryptPsw(new String(registerPsw.getPassword()))+
					"<>"+registerUserName.getText()+"<>"+question.getText()+
					"<>"+answer.getText()));
			
			MSG m = dto.getSc().getMSG();
			if(m.gettOM().equals("confirm")){
				JOptionPane.showMessageDialog(null,
						"Successfully registered!\n" +
						"UserId = "+userId.getText()+"\n" +
						"Password = "+new String(registerPsw.getPassword())+"\n" +
						"NickName = "+registerUserName.getText()+"\n" +
						"Question is '"+question.getText()+"'\n" +
						"Answer is '"+answer.getText()+"'",
						"Information", JOptionPane.INFORMATION_MESSAGE);
				dto.getFrameList().get("RegisterFrame").setVisible(false);
				remove();
			}
			else JOptionPane.showMessageDialog(null,
					m.getSenderId(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void remove(){
		registerUserName.setText("");
		registerPsw.setText("");
		registerConfirmPsw.setText("");
		question.setText("");
		answer.setText("");
	}

	@Override
	public synchronized void login() {
		if(userId.getText().equals("")){
			JOptionPane.showMessageDialog(null,
					"Please input your id", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		else if(!userId.getText().matches("\\d+")){
			JOptionPane.showMessageDialog(null,
					"Id should be INTEGER", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		else if(userId.getText().length() > 9){
			JOptionPane.showMessageDialog(null,
					"The length of Id should be at most 9 bits", "Warning", JOptionPane.WARNING_MESSAGE);
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
			//save Disk data
			DiskData dd = new DiskData();
			dd.setMode(mode);
			dd.setUserId(userId.getText());
			if(!autoLogin.isSelected())
				dd.setPortNum("");
			dd.setPsw(new String(psw.getPassword()));
			dd.setPortNum(portNum.getText());
			dd.setServerIp(serverIp.getText());
			control.getDiskData().saveData(dd);
			//initialize contact list
			initContactList();
			dto.getFrameList().get("LoginFrame").setVisible(false);
			dto.getFrameList().get("MainFrame").setVisible(true);
			//Strat a listener Thread
			dto.setRun(true);
			new ListenerTread().start();
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


	@Override
	public synchronized  void logout() {
		//Notice Sever that user logout, socket on server side will know whether user has logged in.
		dto.getSc().sendMSG(new MSG(null,"logout"));
		try {
			dto.setRun(false);
			if(dto.getSc().getSocket() != null){
				dto.getSc().getSocket().shutdownInput();
				dto.getSc().getSocket().shutdownOutput();
				dto.getSc().getSocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void alterPswFrame(){
		if(userId.getText().equals("")){
			JOptionPane.showMessageDialog(null,
					"Please input your id", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		alterPswAnswer.setEnabled(true);
		alterPswAnswer.setText("");
		((JButton)(dto.getComponentList().get("judgeQuestion"))).setEnabled(true);
		alterPsw.setEnabled(false);
		confirmAlterPsw.setEnabled(false);
		((JButton)(dto.getComponentList().get("alterPsw"))).setEnabled(false);
		dto.getSc().sendMSG(new MSG(userId.getText(),"isUser"));
		MSG m = dto.getSc().getMSG();
		if(m.gettOM().equals("confirm")){
			dto.getFrameList().get("AlterPswFrame").setVisible(true);
			getQuestion();
		}
		else
			JOptionPane.showMessageDialog(null,
				"This User Id not exist!", "Warning", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public synchronized void getQuestion(){
		dto.getSc().sendMSG(new MSG(userId.getText(), "getQuestion"));
		TextMSG m = (TextMSG)(dto.getSc().getMSG());
		if(m.gettOM().equals("confirm")){
			alterPswQuestion.setText(m.getReceiverId());
			answerString = m.getText();
			((JButton)(dto.getComponentList().get("alterPsw"))).setEnabled(false);
			alterPswQuestion.setEnabled(false);
			alterPsw.setEnabled(false);
			confirmAlterPsw.setEnabled(false);
		}
		else{
			JOptionPane.showMessageDialog(null,
					m.getSenderId(), "ERROR", JOptionPane.ERROR_MESSAGE);
			dto.getFrameList().get("AlterPswFrame").setVisible(false);
		}
		
	}

	@Override
	public void judgeQuestion(){
		if(alterPswAnswer.getText().equals(answerString)){
			((JLabel)(dto.getComponentList().get("tipLabel"))).setIcon(null);
			alterPswAnswer.setEnabled(false);
			((JButton)(dto.getComponentList().get("judgeQuestion"))).setEnabled(false);
			alterPsw.setEnabled(true);
			confirmAlterPsw.setEnabled(true);
			((JButton)(dto.getComponentList().get("alterPsw"))).setEnabled(true);
		}
		else
			((JLabel)(dto.getComponentList().get("tipLabel"))).setIcon(Img.getImgIcon("alter/tip.png"));
	}


	@Override
	public synchronized void alterPsw(){
		dto.getSc().sendMSG(new TextMSG(userId.getText(), "alterPsw", null, 
				encryptPsw(new String(confirmAlterPsw.getPassword()))));
		MSG m = dto.getSc().getMSG();
		if(m.gettOM().equals("confirm")){
			JOptionPane.showMessageDialog(null,
					"Success to alter password", "Inform", JOptionPane.INFORMATION_MESSAGE);
			dto.getFrameList().get("AlterPswFrame").setVisible(false);
		}
		else
			JOptionPane.showMessageDialog(null,
					m.getSenderId(), "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public synchronized void initContactList(){
		InitMSG initMsg = (InitMSG)(dto.getSc().getMSG());
		dto.setContactList(initMsg.getContactList());
		//set contact list
		((MainFrame)(dto.getFrameList().get("MainFrame"))).changeContactList(dto.getContactList());
	}
	
	@Override
	public synchronized void sendTextMSG() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void sendGroupTextMSG() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void sendFileMSG() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void sendGroupFileMSG() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void getFile() {
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
	public synchronized void inviteToGroup() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void createGroup() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void joinGroup() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void renameGroup() {
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
	
	@Override
	public void textMSG(){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void groupTextMSG(){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void fileMSG(){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void groupFileMSG(){
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Strar configFrame
	 */
	public void configFrame(){
//		dto.
	}
	
	/**
	 * Strar findFrame
	 */
	public void findFrame(){
		
	}
	
	/**
	 * @author SuperSun
	 * Inner Class 
	 * A thread for monitoring server msg
	 */
	private class ListenerTread extends Thread{
		public synchronized void run(){
			try {
				while(dto.isRun()){
					msg = dto.getSc().getMSG();
					if(msg != null)
						try {
							this.getClass().getMethod(msg.gettOM()).invoke(this);
						} catch (Exception e) {
							e.printStackTrace();
						}
						sleep(100);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	//Unused methods
	public void saveUserName() {}
	public void autoLogin() {}
	public void tipLabel() {}
}
