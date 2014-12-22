package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import bean.Group;
import bean.MSG;
import bean.TextMSG;
import bean.User;
import dto.Dto;

public class MyPopupMenu extends JPopupMenu{
	//User
	private JMenuItem sendUserMessage;
	private JMenuItem renameFriend;
	private JMenuItem deleteFriend;
	//Group
	private JMenuItem createGroup;
	private JMenuItem sendGroupMessage;
	private JMenuItem renameGroup;
	private JMenuItem deleteGroup;
	private JMenuItem inviteFriends;
	//
	private JMenuItem refresh;
	private User user;
	private Group group;
	private Dto dto;
	
	public MyPopupMenu(User user, Dto dto) {
		super();
		initUser();
		initList(false);
		this.user = user;
		this.dto = dto;
	}

	public MyPopupMenu(Dto dto, boolean flag) {
		super();
		initList(flag);
		this.dto = dto;
	}
	
	public MyPopupMenu(Group group, Dto dto) {
		super();
		initGroup();
		initList(true);
		this.group = group;
		this.dto = dto;
	}
	
	private void initList(final boolean flag){
		refresh = new JMenuItem("Refresh");
		if(flag){
			createGroup = new JMenuItem("Create Group");
			createGroup.addActionListener(new ActionListener() {
				public synchronized void actionPerformed(ActionEvent e) {
					String groupId = "";
					while(groupId.equals("")){
						groupId = JOptionPane.showInputDialog("Input your Group id");
						if(groupId.equals("") ||groupId==null){
							JOptionPane.showMessageDialog(null,
									"Please input the group id", "Warning", JOptionPane.WARNING_MESSAGE);
							groupId = "";
						}
						else if(!groupId.matches("\\d+")){
							JOptionPane.showMessageDialog(null,
									"Group id should be INTEGER", "Warning", JOptionPane.WARNING_MESSAGE);
							groupId = "";
						}
						else if(groupId.length() > 9){
							JOptionPane.showMessageDialog(null,
									"The length of group Id should be at most 9 bits", "Warning", JOptionPane.WARNING_MESSAGE);
							groupId = "";
						}
					}
					if(!groupId.equals(""))
						dto.getSc().sendMSG(new MSG(groupId, "isGroup"));
				}
			});
			this.add(createGroup);
		}
		this.add(refresh);
		//Add action listener
		refresh.addActionListener(new ActionListener() {
		public synchronized void actionPerformed(ActionEvent e) {
			dto.getSc().sendMSG(new MSG("","refreshContactList"));
		}
	});
}
	
	private void initUser(){
			sendUserMessage = new JMenuItem("Send Message");
			renameFriend = new JMenuItem("Rename Friend");
			deleteFriend = new JMenuItem("Delete Friend");
			this.add(sendUserMessage);
			this.add(renameFriend);
			this.add(deleteFriend);
		//Add action listener
		sendUserMessage.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				dto.getControl().getClientService().chatFrame(user);
				dto.getFrameList().get("ChatFrame").setExtendedState(JFrame.NORMAL);
			}
		});
		
		renameFriend.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				String givenName = JOptionPane.showInputDialog("Input new givenName");
				dto.getSc().sendMSG(new TextMSG(user.getUserId(), "renameFriend", null, givenName));
			}
		});
		
		deleteFriend.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				dto.getSc().sendMSG(new MSG(user.getUserId(), "deleteFriend"));
			}
		});
	}
	
	private void initGroup(){
			sendGroupMessage = new JMenuItem("Send group Message");
			renameGroup = new JMenuItem("Rename Group");
			deleteGroup = new JMenuItem("Delete Group");
			inviteFriends = new JMenuItem("Invite Friends");
			this.add(sendGroupMessage);
			this.add(renameGroup);
			this.add(deleteGroup);
			this.add(inviteFriends);
		sendGroupMessage.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				dto.getControl().getClientService().chatFrame(group);
				dto.getFrameList().get("ChatFrame").setExtendedState(JFrame.NORMAL);
			}
		});
		
		renameGroup.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				String givenName = JOptionPane.showInputDialog("Input new givenName");
				dto.getSc().sendMSG(new TextMSG(group.getGroupId(), "renameGroup", null, givenName));
			}
		});
		
		deleteGroup.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				dto.getSc().sendMSG(new MSG(group.getGroupId(), "deleteGroup"));
			}
		});
		
		inviteFriends.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				//TODO last to implemet
//				dto.getControl().getClientService().inviteToGroup(group.getGroupId());
			}
		});
	}
}
