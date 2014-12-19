package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import bean.ContactList;
import config.FrameConfig;
import control.Control;

import util.MyList;
import util.MyScrollPane;

public class MainFrame extends MyFrame{
	private JTabbedPane tabPanel;
	private MyScrollPane userPanel;
	private MyScrollPane groupPanel;
	private MyList userList;
	private MyList groupList;

	public MainFrame(FrameConfig fCfg, final Control control) {
		super(fCfg,control);
		//Create Frame
		this.createFrame();
		this.setResizable(false);
//		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
					int i = JOptionPane.showConfirmDialog(null,
							"Do you want to close?", "Warming",
							JOptionPane.YES_NO_OPTION);
					if(Math.abs(i) == 1)
						return;
				control.getClientService().logout();
				System.exit(0);
			}
		});
		userList = new MyList(16);
		userPanel = new MyScrollPane(userList);
		
		groupList = new MyList(16);
		groupPanel = new MyScrollPane(groupList);
	}
	
	public void changeContactList(final ContactList contactlist){
		tabPanel = (JTabbedPane) dto.getComponentList().get("tabPanel");
		tabPanel.removeAll();
		userList.setListData(contactlist.getUsers());
		groupList.setListData(contactlist.getGroups());
		tabPanel.addTab("Friend", userPanel);
		tabPanel.addTab("Group", groupPanel);
		
		//Add MouseListener for contactList
		userList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int index = userList.locationToIndex(e.getPoint());
				userList.setSelectedIndex(index);
				userList.setSelectionForeground(new Color(9,163,220));
				//TODO response mouseClicke
				
			}
		});
		
		groupList.addMouseListener(new MouseAdapter() {
			//Add MouseListener for groupList
			public void mouseClicked(MouseEvent e) {
				int index = groupList.locationToIndex(e.getPoint());
				groupList.setSelectedIndex(index);
				groupList.setSelectionForeground(new Color(9,163,220));
				//TODO response mouseClicke
				
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		paintComponents(g);
	}
}
