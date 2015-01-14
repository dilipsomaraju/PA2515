package ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import util.MyList;
import util.MyPopupMenu;
import util.MyScrollPane;
import bean.ContactList;
import bean.Group;
import bean.User;
import config.FrameConfig;
import control.Control;

public class MainFrame extends MyFrame{
	private JTabbedPane tabPanel;
	private MyScrollPane userPanel;
	private MyScrollPane groupPanel;
	private MyList userList;
	private MyList groupList;
	private MyPopupMenu userPopMenu;
	private MyPopupMenu groupPopMenu;
	private MyPopupMenu userListPopMenu;
	private MyPopupMenu groupListPopMenu;

	public MainFrame(FrameConfig fCfg, final Control control) {
		super(fCfg,control);
		//Create Frame
		this.createFrame();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//TODO Using config to judge what should do when closing
				int i = JOptionPane.showConfirmDialog(null,
						"Press YES to close\n" +
						"Press NO to narrow to system tray\n" +
						"Prese close to cancle", "Warming",
						JOptionPane.YES_NO_OPTION);
				//Cancle: PRESS CLOSE
				if(i == -1)
					return;
				//Close: PRESS YSE
				else if(i == 0){
					control.getClientService().logout();
					System.exit(0);
				}
				//System tray: PRESS NO
				else if(i == 1)
					MainFrame.this.setVisible(false);
				if(SystemTray.isSupported()){
					final SystemTray tray = SystemTray.getSystemTray();
					Image image=new ImageIcon("graphic/tray.png").getImage();
					PopupMenu pop = new PopupMenu();
					MenuItem show = new MenuItem("Recover");
					MenuItem exit = new MenuItem("Exit");
					final TrayIcon trayIcon=new TrayIcon(image, "LanChat Client", pop);
					show.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							tray.remove(trayIcon);
							MainFrame.this.setVisible(true);
							MainFrame.this.setExtendedState(JFrame.NORMAL);
							MainFrame.this.toFront();
						}
					});
					exit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							control.getClientService().logout();
							tray.remove(trayIcon);
							System.exit(0);
						}
					});
					pop.add(show);
					pop.add(exit);
					trayIcon.setImageAutoSize(true);
					trayIcon.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) { 
							if (e.getClickCount() == 2) {
								tray.remove(trayIcon);
								MainFrame.this.setVisible(true);
								MainFrame.this.setExtendedState(JFrame.NORMAL);
								MainFrame.this.toFront();
							}
						}
					});
					try {
						tray.add(trayIcon);
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		
	}
	
	public void changeContactList(ContactList contactlist){
		tabPanel = (JTabbedPane) dto.getComponentList().get("tabPanel");
		tabPanel.removeAll();
		userList = new MyList(14);
		userPanel = new MyScrollPane(userList);
		groupList = new MyList(14);
		groupPanel = new MyScrollPane(groupList);
		userList.setListData(contactlist.getUsers());
		groupList.setListData(contactlist.getGroups());
		userPanel.setBorder(null);
		groupPanel.setBorder(null);
		tabPanel.setBorder(null);
		tabPanel.addTab("Friend", userPanel);
		tabPanel.addTab("Group", groupPanel);
		//Set userId - userGivenName
		for(User u : contactlist.getUsers())
			dto.getIdUser().put(u.getUserId(), u);
		
		for(Group g : contactlist.getGroups())
			dto.getIdGroup().put(g.getGroupId(), g);
		
		//Add MouseListener for contactList
		userList.addMouseListener(new MouseAdapter() {
			public synchronized void mouseClicked(MouseEvent e) {
				if(e.getY() <= (userList.getLastVisibleIndex()+1)*userList.getFixedCellHeight()){
					int index = userList.locationToIndex(e.getPoint());
					userList.setSelectedIndex(index);
					userList.setSelectionForeground(new Color(9,163,220));
					
					//Left button double click
					if(e.getButton() == 1 && e.getClickCount()==2){
						dto.getControl().getClientService().chatFrame(((User)userList.getSelectedValue()));
						dto.getFrameList().get("ChatFrame").setExtendedState(JFrame.NORMAL);
						ChatPanel p = (ChatPanel) dto.getChatPanelList().get(((User)userList.getSelectedValue()).getUserId());
						JTabbedPane tab = ((ChatFrame)(dto.getFrameList().get("ChatFrame"))).getTabPanel();
						tab.setSelectedComponent(p);
					}
					else if(e.getButton() == 3 && userList.getSelectedIndex() >= 0){
						userPopMenu = new MyPopupMenu(((User)userList.getSelectedValue()), dto);
						userPopMenu.show(userList, e.getX(), e.getY());
					}
				}
				else {
					userList.setSelectedIndex(-1);
					userList.setSelectionForeground(new Color(51,51,51));
					if(e.getButton() ==3){
						userListPopMenu = new MyPopupMenu(dto,false);
						userListPopMenu.show(userList, e.getX(), e.getY());
					}
				}
			}
		});
		
		groupList.addMouseListener(new MouseAdapter() {
			//Add MouseListener for groupList
			public synchronized void mouseClicked(MouseEvent e) {
				if(e.getY() <= (groupList.getLastVisibleIndex()+1)*groupList.getFixedCellHeight()){
					int index = groupList.locationToIndex(e.getPoint());
					groupList.setSelectedIndex(index);
					groupList.setSelectionForeground(new Color(9,163,220));
					
					//Left button double click
					if(e.getButton() == 1 && e.getClickCount()==2){
						dto.getControl().getClientService().chatFrame(((Group)groupList.getSelectedValue()));
						dto.getFrameList().get("ChatFrame").setExtendedState(JFrame.NORMAL);
						ChatPanel p = (ChatPanel) dto.getChatPanelList().get("g"+((Group)groupList.getSelectedValue()).getGroupId());
						JTabbedPane tab = ((ChatFrame)(dto.getFrameList().get("ChatFrame"))).getTabPanel();
						tab.setSelectedComponent(p);
					}
					else if(e.getButton() == 3 && groupList.getSelectedIndex() >= 0){
						groupPopMenu = new MyPopupMenu(((Group)groupList.getSelectedValue()), dto);
						groupPopMenu.show(groupList, e.getX(), e.getY());
					}
				}
				else {
					groupList.setSelectedIndex(-1);
					groupList.setSelectionForeground(new Color(51,51,51));
					if(e.getButton() ==3){
						groupListPopMenu = new MyPopupMenu(dto,true);
						groupListPopMenu.show(groupList, e.getX(), e.getY());
					}
				}
			}
		});
		//Repaint tabPanel
		tabPanel.repaint();
	}
	
	public JTabbedPane getTabPanel() {
		return tabPanel;
	}

	@Override
	public void paint(Graphics g) {
		paintComponents(g);
	}
}
