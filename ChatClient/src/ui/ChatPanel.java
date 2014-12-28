package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import util.MyScrollPane;
import bean.FileMSG;
import bean.Group;
import bean.MSG;
import bean.TextMSG;
import bean.User;
import dto.Dto;

/**
 * @author SuperSun
 * Chat panel
 */
public class ChatPanel extends JPanel{
	private MyScrollPane showPanel;
	private MyScrollPane inputPanel;
	private JTextPane textPanel;
	private JTextArea textArea;
	private StyledDocument doc;
	private SimpleAttributeSet attrSet;
	private Dto dto;
	private JButton sendFriendTextMSG;
	private JButton sendFriendFileMSG;
	private JButton sendGroupTextMSG;
	private JButton sendGroupFileMSG;
	private JFileChooser fileChooser;
	
	public ChatPanel(final User user, final Dto dto) {
		super(null);
		this.dto = dto;
		this.setOpaque(false);
		initPanel();
		sendFriendTextMSG = new JButton("sendFriendTextMSG");
		//TODO locattion, picture
		this.add(sendFriendTextMSG);
		sendFriendTextMSG.setBounds(340, 426, 200, 23);
		
		sendFriendFileMSG = new JButton("sendFriendFileMSG");
		//TODO locattion, picture
		sendFriendFileMSG.setBounds(3, 301, 200, 23);
		this.add(sendFriendFileMSG);
		
		//Send Text MSG to friend
		sendFriendTextMSG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textArea.getText().equals("")||textArea.getText() == null){
					JOptionPane.showMessageDialog(null,
							"Empty message", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				TextMSG textMSG = new TextMSG(dto.getDiskData().getUser().getUserId(),"sendTextMSG",
						user.getUserId(),textArea.getText());
				dto.getSc().sendMSG(textMSG);
				textArea.setText("");
			}
		});
		//Send File MSG to Firned
		sendFriendFileMSG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null; 
				fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select a File");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fileChooser.showDialog(null,"Send");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					if(file.isFile()){
						FileMSG fileMSG = new FileMSG(dto.getDiskData().getUser().getUserId(),"sendFileMSG",
								user.getUserId(),file);
						dto.getSc().sendMSG(fileMSG);
					}
					else
						JOptionPane.showMessageDialog(null,
								"File not exist", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public ChatPanel(final Group group, final Dto dto) {
		super(null);
		this.dto = dto;
		this.setOpaque(false);
		initPanel();
		sendGroupTextMSG = new JButton("sendGroupTextMSG");
		//TODO locattion, picture
		sendGroupTextMSG.setBounds(340, 426, 200, 23);
		this.add(sendGroupTextMSG);
		sendGroupFileMSG = new JButton("sendGroupFileMSG");
		//TODO locattion, picture
		sendGroupFileMSG.setBounds(3, 301, 200, 23);
		this.add(sendGroupFileMSG);
		
		sendGroupTextMSG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textArea.getText().equals("")||textArea.getText() == null){
					JOptionPane.showMessageDialog(null,
							"Empty message", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				TextMSG textMSG = new TextMSG(dto.getDiskData().getUser().getUserId(),"sendGroupTextMSG",
						group.getGroupId(),textArea.getText());
				dto.getSc().sendMSG(textMSG);
				textArea.setText("");
			}
		});
		
		sendGroupFileMSG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null; 
				fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select a File");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fileChooser.showDialog(fileChooser,"Send");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					if(file.isFile()){
						FileMSG fileMSG = new FileMSG(dto.getDiskData().getUser().getUserId(),"sendGroupFileMSG",
								group.getGroupId(),file);
						dto.getSc().sendMSG(fileMSG);
					}
					else
						JOptionPane.showMessageDialog(null,
								"File not exist", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public void initPanel(){
		textPanel = new JTextPane();
		textArea = new JTextArea();
		textArea.grabFocus();
		this.setVisible(true);
		textPanel.setEditable(false);
		textArea.setLineWrap(true);
		showPanel = new MyScrollPane(textPanel, null);
		inputPanel = new MyScrollPane(textArea, null);
		this.setLayout(null);
		//TODO set location
		showPanel.setBounds(3,0,540,300);
		inputPanel.setBounds(3,325,540,100);
		this.add(showPanel);
		this.add(inputPanel);
		doc = textPanel.getStyledDocument();
		attrSet = new SimpleAttributeSet(); 
	}
	
	public String getContext(){
		return textArea.getText();
	}
	//add TEXT msg
	public void addMSG(String senderNickName, String time, String context, boolean isSender){
		try {
			if(isSender)
				StyleConstants.setForeground(attrSet, Color.GREEN);
			else
				StyleConstants.setForeground(attrSet, Color.BLUE);
			doc.insertString(doc.getLength(), senderNickName+"  "+time+"\n", attrSet);
			StyleConstants.setForeground(attrSet, Color.BLACK);
			doc.insertString(doc.getLength(), context+"\n", attrSet);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	//ADD File MSG
	public void addMSG(String senderNickName, String time, String fileName, final String link, boolean isSender){
		try {
			if(isSender){
				StyleConstants.setForeground(attrSet, Color.GREEN);
				doc.insertString(doc.getLength(), senderNickName+"  "+time+"\n", attrSet);
				StyleConstants.setForeground(attrSet, Color.GRAY);
				doc.insertString(doc.getLength(), "You uploaded "+fileName+"\n", attrSet);
			}
			else{
				StyleConstants.setForeground(attrSet, Color.BLUE);
				doc.insertString(doc.getLength(), senderNickName+"  "+time+"\n", attrSet);
				StyleConstants.setForeground(attrSet, Color.BLACK);
				doc.insertString(doc.getLength(), "Uploaded "+fileName+"  ", attrSet);
				final JLabel l = new JLabel("Donwload");
				l.setForeground(Color.GREEN);
				l.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						dto.getSc().sendMSG(new MSG(link,"getFile"));
					}
					//Mouse enter or exit, show another color
					public void mouseEntered(MouseEvent e2){
						l.setForeground(Color.BLUE);
					}
					public void mouseExited(MouseEvent e3){
						l.setForeground(Color.GREEN);
					}
				});
				textPanel.setCaretPosition(doc.getLength()); 
				textPanel.insertComponent(l);
				doc.insertString(doc.getLength(), "\n", attrSet);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
