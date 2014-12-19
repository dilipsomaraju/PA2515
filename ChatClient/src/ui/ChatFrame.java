package ui;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import config.FrameConfig;
import control.Control;


public class ChatFrame extends MyFrame{

	public ChatFrame(FrameConfig frameCfg, Control control) {
		super(frameCfg, control);
		this.createFrame();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//TODO add two panel: one fixed input panel, one unfixed JTabbedPane
		
	}
	
	public void addTabPanel(JPanel panel){
		
	}
	
	public void removeTabPanel(JPanel panel){
		
	}

	@Override
	public void paint(Graphics g) {
		paintComponents(g);
	}

}
