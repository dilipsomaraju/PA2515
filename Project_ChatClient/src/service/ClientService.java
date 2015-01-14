package service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import ui.ChatFrame;
import ui.FindFrame;
import ui.ChatPanel;
import ui.Img;
import ui.MainFrame;
import bean.DiskData;
import bean.FileMSG;
import bean.Group;
import bean.InitMSG;
import bean.InviteToGroupMSG;
import bean.MSG;
import bean.MessageMSG;
import bean.TextMSG;
import bean.User;
import control.Control;
import dto.Dto;

public class ClientService implements Service{
	private Dto dto;
	private Control control;
	//DiskData
	private DiskData dd;
	private String mode;
	private User user;
	//connect parameter
	private JTextField serverIp;
	private JTextField portNum;
	//login parameter
	private JTextField userId;
	private JPasswordField psw;
	private JCheckBox saveUserName;
	private JCheckBox autoLogin;
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
	//config parameter
	private JCheckBox configAutoLogin;
	private JCheckBox configSaveUserName;
	//find parameter
	private JRadioButton findUser;
	private JRadioButton findGroup;
	private JRadioButton findId;
	private JRadioButton findNickName;
	private JTextField clue;
	//msg got from server
	private MSG msg;
	//flag
	private static boolean notice = true;

//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ClientService(Dto dto, Control control){
		this.dto = dto;
		this.control = control;
		dd = dto.getDiskData();
		mode = dd.getMode();
		user = dd.getUser();
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
		//Config Frame components
		configSaveUserName = ((JCheckBox)(dto.getComponentList().get("configSaveUserName")));
		configAutoLogin = ((JCheckBox)(dto.getComponentList().get("configAutoLogin")));
		//find Frame compenents
		findUser = (JRadioButton) dto.getComponentList().get("findUser");
		findGroup = (JRadioButton) dto.getComponentList().get("findGroup");
		findId = (JRadioButton) dto.getComponentList().get("findId");
		findNickName = (JRadioButton) dto.getComponentList().get("findNickName");
		clue = ((JTextField)(dto.getComponentList().get("clue")));
		//Setting
		serverIp.setText(dto.getDiskData().getServerIp());
		portNum.setText(dto.getDiskData().getPortNum());
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(findUser);
		bg1.add(findGroup);
		findUser.setSelected(true);
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(findId);
		bg2.add(findNickName);
		findId.setSelected(true);
		
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
				
			}
		});
		saveUserName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
				if(mode.equals("") || mode == null){
					saveUserName.setSelected(false);
					autoLogin.setSelected(false);
					configSaveUserName.setSelected(false);
					configAutoLogin.setSelected(false);
					userId.grabFocus();
				}
				else if(mode.equals("saveUserName")){
					saveUserName.setSelected(true);
					autoLogin.setSelected(false);
					configSaveUserName.setSelected(true);
					configAutoLogin.setSelected(false);
					if(user != null){
						userId.setText(user.getUserId());
						psw.grabFocus();
					}
				}
				else if(mode.equals("autoLogin")){
					saveUserName.setSelected(true);
					saveUserName.setEnabled(false);
					autoLogin.setSelected(true);
					configSaveUserName.setSelected(true);
					configSaveUserName.setEnabled(false);
					configAutoLogin.setSelected(true);
					if(user != null){
						userId.setText(user.getUserId());
						psw.setText(user.getPsw());
					}
					else userId.grabFocus();
				}
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
		if(registerUserName.getText().equals("") || registerUserName.getText() == null)
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
		else if(question.getText().equals("") || question.getText() == null)
			JOptionPane.showMessageDialog(null,
					"Please input your question", "Warning", JOptionPane.WARNING_MESSAGE);
		else if(answer.getText().equals("") || answer.getText() == null)
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
			dd.setMode(mode);
			user.setUserId(userId.getText());
			if(!autoLogin.isSelected())
				dd.setPortNum("");
			user.setPsw(new String(psw.getPassword()));
			dd.setUser(user);
			dd.setPortNum(portNum.getText());
			dd.setServerIp(serverIp.getText());
			control.getDiskData().saveData(dd);
			//refresh contact list
			dto.getSc().sendMSG(new MSG("","refreshContactList"));
			dto.getFrameList().get("LoginFrame").setVisible(false);
			dto.getFrameList().get("MainFrame").setVisible(true);
			//Strat a listener Thread
			dto.setRun(true);
			new ListenerTread(this).start();
		}
		else JOptionPane.showMessageDialog(null,
				m.getSenderId(), "ERROR", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public synchronized void refreshContactList(){
		int index = 0;
		if(((MainFrame)(dto.getFrameList().get("MainFrame"))).getTabPanel() != null){
			index = ((MainFrame)(dto.getFrameList().get("MainFrame"))).getTabPanel().getSelectedIndex();
		}
		InitMSG initMsg = (InitMSG)(msg);
		if(initMsg.getSenderId().equals("confirm")){
			dto.setContactList(initMsg.getContactList());
			//set contact list
			((MainFrame)(dto.getFrameList().get("MainFrame"))).changeContactList(dto.getContactList());
		}
		else JOptionPane.showMessageDialog(null,
				initMsg.getSenderId(), "ERROR", JOptionPane.ERROR_MESSAGE);
		((MainFrame)(dto.getFrameList().get("MainFrame"))).getTabPanel().setSelectedIndex(index);
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
	
	public synchronized void broadcast(){
		TextMSG textMSG = (TextMSG)msg;
		JTabbedPane tab = ((ChatFrame)(dto.getFrameList().get("ChatFrame"))).getTabPanel();
		ChatPanel cp = (ChatPanel) dto.getChatPanelList().get(textMSG.getSenderId());
		int index = tab.getSelectedIndex();
		if(textMSG.getText().equals("logout") && cp != null){
			int i = tab.indexOfComponent(cp);
			if(i == -1) return;
			tab.remove(cp);
		}
		//refresh contact list
		dto.getSc().sendMSG(new MSG("","refreshContactList"));
		tab.setSelectedIndex(index);
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
		if(!alterPsw.getText().equals(confirmAlterPsw.getText())){
			JOptionPane.showMessageDialog(null,
					"The two password are not match", "ERROR", JOptionPane.ERROR_MESSAGE);
			alterPsw.setText("");
			confirmAlterPsw.setText("");
			return;
		}
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
	

	/**
	 * Strar findFrame
	 */
	public void findFrame(){
		dto.getFrameList().get("FindFrame").setVisible(true);
	}

	@Override
	public synchronized void find() {
		String clue = this.clue.getText();
		if(clue.equals("")){
			JOptionPane.showMessageDialog(null,
					"Please input a clue", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String tableName = null;
		String attribute = null;
		if(findUser.isSelected()){
			if(clue.equals(userId.getText())){
				JOptionPane.showMessageDialog(null,
						"Why find yourself?", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			tableName = "userInfo";
			attribute = "userId";
			dto.setFindFlag("user");
		}
		
		else{
			tableName = "groupInfo";
			attribute = "groupId";
			dto.setFindFlag("group");
		}
		if(findNickName.isSelected()) attribute = "nickName";
		TextMSG textMSG = new TextMSG(tableName,"find",attribute,clue);
		dto.getSc().sendMSG(textMSG);
	}

	public synchronized void changeFindList(){
		InviteToGroupMSG imsg = (InviteToGroupMSG)msg;
		if(imsg.getSenderId().equals("confirm")){
			((FindFrame)(dto.getFrameList().get("FindFrame"))).changeResultList((Vector<String>) imsg.getReceiverIds());
		}
		else
			JOptionPane.showMessageDialog(null,
					imsg.getSenderId(), "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public synchronized void addFriend() {
		popFeedback(false);
	}

	@Override
	public synchronized void renameFriend() {
		popFeedback(true);
	}

	@Override
	public synchronized void deleteFriend() {
		popFeedback(false);
	}

	public synchronized void isGroup(){
		if(msg.getSenderId().equals("This group id is exist"))
			JOptionPane.showMessageDialog(null,
					msg.getSenderId(), "ERROR", JOptionPane.ERROR_MESSAGE);
		else {
			String nickName = "";
			while(nickName.equals("")){
				nickName = JOptionPane.showInputDialog("Input the Group nick name\n16 characters limited");
				if(nickName.length() > 16)
					nickName = "";
			}
			String givenName = "";
			while(givenName.equals("")){
				givenName = JOptionPane.showInputDialog("Input the Group given name\n16 characters limited");
				if(givenName.length() > 16)
					givenName = "";
			}
			dto.getSc().sendMSG(new TextMSG(msg.getSenderId(),"createGroup",nickName,givenName));
		}
	}
	
	@Override
	public synchronized void createGroup() {
		popFeedback(false);
	}
	
	@Override
	public synchronized void addGroup() {
		popFeedback(false);
	}
	
	@Override
	public synchronized void renameGroup() {
		popFeedback(true);
	}

	@Override
	public synchronized void deleteGroup() {
		popFeedback(false);
	}

	
	@Override
	public synchronized void textMSG(){
		boolean isSender = false;
		MessageMSG mmsg = (MessageMSG)msg;
		//Get friend id
		String userId = null;
		if(mmsg.getSenderId().equals(user.getUserId())){
			userId = mmsg.getReceiverId();
			isSender = true;
		}
		else if(mmsg.getReceiverId().equals(user.getUserId())){
			userId = mmsg.getSenderId();
			isSender = false;
		}
		
		//If this panel didn't selected or exist, Notice
		JTabbedPane tab = ((ChatFrame)(dto.getFrameList().get("ChatFrame"))).getTabPanel();
		ChatPanel cp = (ChatPanel) dto.getChatPanelList().get(userId);
		if(cp == null){
			cp =new ChatPanel(dto.getIdUser().get(userId),dto);
			//Set groupId - chatPanel
			dto.getChatPanelList().put(userId, cp);
		}
		cp.addMSG(mmsg.getSenderNickName(), mmsg.getTime(), mmsg.getText(), isSender);
		int index = tab.indexOfComponent(cp);
		if(index == tab.getSelectedIndex() && index != -1){
			((ChatFrame)(dto.getFrameList().get("ChatFrame"))).setExtendedState(JFrame.NORMAL);
			((ChatFrame)(dto.getFrameList().get("ChatFrame"))).setVisible(true);
			return;
		}
		if(notice){
			notice = false;
			String givenName = dto.getIdUser().get(userId).getGivenName();
			int i = JOptionPane.showConfirmDialog(null,
					"Friend: '" + givenName +"' have some unread messages", "New Message",
					JOptionPane.YES_OPTION);
			if(i == -1 || i ==0)
				notice = true;
		}
	}
	
	@Override
	public synchronized void groupTextMSG(){
		boolean isSender = false;
		MessageMSG mmsg = (MessageMSG)msg;
		//Get group id
		String groupId = mmsg.getReceiverId();
		if(mmsg.getSenderId().equals(user.getUserId()))
			isSender = true;
		else 
			isSender = false;
		//If this panel didn't selected or exist, Notice
		JTabbedPane tab = ((ChatFrame)(dto.getFrameList().get("ChatFrame"))).getTabPanel();
		ChatPanel cp = (ChatPanel) dto.getChatPanelList().get("g"+groupId);
		if(cp == null){
			cp =new ChatPanel(dto.getIdGroup().get(groupId),dto);
			//Set groupId - chatPanel
			dto.getChatPanelList().put("g"+groupId, cp);
		}
		
		cp.addMSG(mmsg.getSenderNickName(), mmsg.getTime(), mmsg.getText(), isSender);
		int index = tab.indexOfComponent(cp);
		if(index == tab.getSelectedIndex() && index != -1){
			((ChatFrame)(dto.getFrameList().get("ChatFrame"))).setExtendedState(JFrame.NORMAL);
			((ChatFrame)(dto.getFrameList().get("ChatFrame"))).setVisible(true);
			return;
		}
		if(notice){
			notice = false;
			String givenName = dto.getIdGroup().get(groupId).getGivenName();
			int i = JOptionPane.showConfirmDialog(null,
					"Group: '" + givenName +"' have some unread messages", "New Group Message",
					JOptionPane.YES_OPTION);
			if(i == -1 || i ==0)
				notice = true;
		}
	}
	
	@Override
	public synchronized void fileMSG(){
		boolean isSender = false;
		MessageMSG mmsg = (MessageMSG)msg;
		//Get friend id
		String userId = null;
		if(mmsg.getSenderId().equals(user.getUserId())){
			userId = mmsg.getReceiverId();
			isSender = true;
		}
		else if(mmsg.getReceiverId().equals(user.getUserId())){
			userId = mmsg.getSenderId();
			isSender = false;
		}
		
		//If this panel didn't selected or exist, Notice
		JTabbedPane tab = ((ChatFrame)(dto.getFrameList().get("ChatFrame"))).getTabPanel();
		ChatPanel cp = (ChatPanel) dto.getChatPanelList().get(userId);
		if(cp == null){
			cp =new ChatPanel(dto.getIdUser().get(userId),dto);
			//Set groupId - chatPanel
			dto.getChatPanelList().put(userId, cp);
		}
		cp.addMSG(mmsg.getSenderNickName(), mmsg.getTime(), mmsg.getText(),mmsg.getLink(), isSender);
		int index = tab.indexOfComponent(cp);
		if(index == tab.getSelectedIndex() && index != -1){
			((ChatFrame)(dto.getFrameList().get("ChatFrame"))).setExtendedState(JFrame.NORMAL);
			((ChatFrame)(dto.getFrameList().get("ChatFrame"))).setVisible(true);
			return;
		}
		if(notice){
			notice = false;
			String givenName = dto.getIdUser().get(userId).getGivenName();
			int i = JOptionPane.showConfirmDialog(null,
					"Friend: '" + givenName +"' have some unread messages", "New Message",
					JOptionPane.YES_OPTION);
			if(i == -1 || i ==0)
				notice = true;
		}
		
	}
	
	@Override
	public synchronized void groupFileMSG(){
		boolean isSender = false;
		MessageMSG mmsg = (MessageMSG)msg;
		//Get group id
		String groupId = mmsg.getReceiverId();
		if(mmsg.getSenderId().equals(user.getUserId()))
			isSender = true;
		else 
			isSender = false;
		//If this panel didn't selected or exist, Notice
		JTabbedPane tab = ((ChatFrame)(dto.getFrameList().get("ChatFrame"))).getTabPanel();
		ChatPanel cp = (ChatPanel) dto.getChatPanelList().get("g"+groupId);
		if(cp == null){
			cp =new ChatPanel(dto.getIdGroup().get(groupId),dto);
			//Set groupId - chatPanel
			dto.getChatPanelList().put("g"+groupId, cp);
		}
		
		cp.addMSG(mmsg.getSenderNickName(), mmsg.getTime(), mmsg.getText(),mmsg.getLink(), isSender);
		int index = tab.indexOfComponent(cp);
		if(index == tab.getSelectedIndex() && index != -1){
			((ChatFrame)(dto.getFrameList().get("ChatFrame"))).setExtendedState(JFrame.NORMAL);
			((ChatFrame)(dto.getFrameList().get("ChatFrame"))).setVisible(true);
			return;
		}
		if(notice){
			notice = false;
			String givenName = dto.getIdGroup().get(groupId).getGivenName();
			int i = JOptionPane.showConfirmDialog(null,
					"Group: '" + givenName +"' have some unread messages", "New Group Message",
					JOptionPane.YES_OPTION);
			if(i == -1 || i ==0)
				notice = true;
		}
		
	}
	
	@Override
	public synchronized void getFile() {
		FileMSG fmsg = (FileMSG)msg;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("SAVE FILE");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showDialog(null,"SAVE");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fi = fileChooser.getSelectedFile();
			String filePath = fi.getAbsolutePath()+"\\"+fmsg.getFile().getName();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
				oos.writeObject(fmsg.getFile());
				oos.flush();
				oos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Start chat frame
	 */
	public void chatFrame(User user){
		ChatFrame cf = (ChatFrame)(dto.getFrameList().get("ChatFrame"));
		ChatPanel p = (ChatPanel) dto.getChatPanelList().get(user.getUserId());
		if(p == null){
			//if this id related panel do not exist, create a new one
			p =new ChatPanel(user,dto);
			//Set userId - chatPanel
			dto.getChatPanelList().put(user.getUserId(), p);
		}
		//ADD this panel into Chat Frame's tab panel
		cf.addTabPanel(user.getGivenName(), p);
		//start Chat Frame
		cf.setVisible(true);
	}
	
	/**
	 * Start chat frame
	 */
	public void chatFrame(Group group){
		ChatFrame cf = (ChatFrame)(dto.getFrameList().get("ChatFrame"));
		ChatPanel p = (ChatPanel) dto.getChatPanelList().get("g"+group.getGroupId());
		if(p == null){
			//if this id related panel do not exist, create a new one
			p =new ChatPanel(group,dto);
			//Set groupId - chatPanel
			dto.getChatPanelList().put("g"+group.getGroupId(), p);
		}
		//ADD this panel into Chat Frame's tab panel
		cf.addTabPanel("G:"+group.getGivenName(), p);
		//start Chat Frame
		cf.setVisible(true);
	}
	
	/**
	 * Strar configFrame
	 */
	public void configFrame(){
		dto.getFrameList().get("ConfigFrame").setVisible(true);
	}
	
	public void saveUserName() {
		if(saveUserName.isSelected()){
			mode = "saveUserName";
			configSaveUserName.setSelected(true);
		}
		else{
			mode = "";
			configSaveUserName.setSelected(false);
		}
		dd.setMode(mode);
	}
	
	public void autoLogin() {
		if(autoLogin.isSelected()){
			mode = "autoLogin";
			saveUserName.setSelected(true);
			saveUserName.setEnabled(false);
			configSaveUserName.setSelected(true);
			configSaveUserName.setEnabled(false);
			configAutoLogin.setSelected(true);
		}
		else{
			saveUserName.setEnabled(true);
			configSaveUserName.setEnabled(true);
			mode = "saveUserName";
		}
		dd.setMode(mode);
	}
	
	public void configSaveUserName() {
		if(configSaveUserName.isSelected())
			mode = "saveUserName";
		else mode = "";
		dd.setMode(mode);
		control.getDiskData().saveData(dd);
	}
	
	public void configAutoLogin() {
		if(configAutoLogin.isSelected()){
			mode = "autoLogin";
			configSaveUserName.setSelected(true);
			configSaveUserName.setEnabled(false);
		}
		else{
			configSaveUserName.setEnabled(true);
			mode = "saveUserName";
		}
		dd.setMode(mode);
		control.getDiskData().saveData(dd);
	}
	
	public MSG getMsg() {
		return msg;
	}
	
	private void popFeedback(boolean flag){
		if(msg.getSenderId().equals("Something wrong in accessing Database")||
				msg.getSenderId().equals("You have already added this user/group"))
			JOptionPane.showMessageDialog(null, msg.getSenderId(),"error",
					JOptionPane.ERROR_MESSAGE);
		else {
				ChatPanel p = (ChatPanel) dto.getChatPanelList().get(msg.getSenderId());
				JTabbedPane tab = ((ChatFrame)(dto.getFrameList().get("ChatFrame"))).getTabPanel();
				int index = tab.getSelectedIndex();
				dto.getSc().sendMSG(new MSG("","refreshContactList"));
				int i = tab.indexOfComponent(p);
				if(i == -1) return;
				tab.remove(p);
				if(flag)
					tab.addTab(formattStr(((TextMSG)msg).getText()), p);
				tab.setSelectedIndex(index);
			}
	}
	//Unused methods
	public void tipLabel() {}
	
	/**
	 * @author SuperSun
	 * Inner Class 
	 * A thread for monitoring server msg
	 */
	private class ListenerTread extends Thread{
		private ClientService clientService;
		public ListenerTread(ClientService clientService){
			this.clientService = clientService;
		}
		public synchronized void run(){
			try {
				while(dto.isRun()){
					msg = dto.getSc().getMSG();
					if(msg != null){
						try {
							clientService.getClass().getMethod(msg.gettOM()).invoke(clientService);
						} catch (Exception e) {
							e.printStackTrace();
						}
						sleep(100);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private String formattStr(String str){
		String s = str;
		while(s.length() < 18)
			s = ">"+s;
		return s;
	}
	
	public synchronized void inviteToGroupFrame() {
		// TODO One of LAST TO IMPLEMENT
		
	}
	
	@Override
	public synchronized void inviteToGroup(String groupId) {
		// TODO One of LAST TO IMPLEMENT
		
	}
	
	@Override
	public synchronized void deleteGroupMember() {
		// TODO LAST LAST to implement
		
	}
}
