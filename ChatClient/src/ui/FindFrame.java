package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import bean.TextMSG;

import util.MyList;
import util.MyScrollPane;
import config.FrameConfig;
import control.Control;

public class FindFrame extends MyFrame{
	
	private MyList idList;
	private MyScrollPane listPanel;
	
	public FindFrame(FrameConfig frameCfg, Control control){
		super(frameCfg, control);
		this.createFrame();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		idList = new MyList(14);
		listPanel = new MyScrollPane(idList);
		panel.add(listPanel);
	}

	public void changeResultList(Vector<String> list){
		panel.remove(listPanel);
		idList = new MyList(14);
		idList.setListData(list);
		listPanel = new MyScrollPane(idList);
		//TODO  set Bounds
		listPanel.setBounds(25, 200, 225, 200);
		panel.add(listPanel);
		
		idList.addMouseListener(new MouseAdapter() {
			public synchronized void mouseClicked(MouseEvent e) {
				if(e.getY() <= (idList.getLastVisibleIndex()+1)*idList.getFixedCellHeight()){
					int index = idList.locationToIndex(e.getPoint());
					idList.setSelectedIndex(index);
					idList.setSelectionForeground(new Color(9,163,220));
					
					//Left button double click
					if(e.getButton() == 1 && e.getClickCount()==2){
						int i = JOptionPane.showConfirmDialog(null,
								"Press YES to ADD Selected object\n" +
								"Press NO or Close to cancle", "Warming",
								JOptionPane.YES_NO_OPTION);
						if(Math.abs(i) == 1)return;
						String givenName = "";
						while(givenName.equals("")){
							givenName = JOptionPane.showInputDialog("Input the given name\n16 characters limited");
							if(givenName.length() > 16)
								givenName = "";
						}
						if(!givenName.equals("")){
							String id = ((String) (idList.getSelectedValue())).split(" ")[0];
							if(dto.getFindFlag().equals("user"))
								dto.getSc().sendMSG(new TextMSG(id, "addFriend", null, givenName));
							else
								dto.getSc().sendMSG(new TextMSG(id, "addGroup", null, givenName));
						}
					}
				}
				else {
					idList.setSelectedIndex(-1);
					idList.setSelectionForeground(new Color(51,51,51));
				}
			}
		});
		listPanel.repaint();
		panel.repaint();
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		paintComponents(g);
	}
}
