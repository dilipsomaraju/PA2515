package ui;

import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import config.FrameConfig;
import control.Control;


public class ChatFrame extends MyFrame{
	private JTabbedPane tabPanel;
	
	public ChatFrame(FrameConfig frameCfg, Control control) {
		super(frameCfg, control);
		this.createFrame();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(tabPanel.getTabCount() == 1){
					tabPanel.removeAll();
					ChatFrame.this.setVisible(false);
					return;
				}
				int i = JOptionPane.showConfirmDialog(null,
						"Close the chat frame?\n" +
						"Press YES to close the whole frame\n" +
						"Press NO to close current panel\n" +
						"Prese close to cancle", "Warming",
						JOptionPane.YES_NO_OPTION);
				//CLOSE: cancle
				if(i == -1);
				//YSE: close the whole frame
				if(i == 0){
					tabPanel.removeAll();
					ChatFrame.this.setVisible(false);
				}
				//NO: close current panel
				if(i == 1){
					int index = tabPanel.getSelectedIndex();
					tabPanel.remove(index);
				}
			}
		});
	}
	
	public void addTabPanel(String givenName, JPanel panel){
		tabPanel = (JTabbedPane) dto.getComponentList().get("chatTabPanel");
		tabPanel. setTabPlacement(JTabbedPane.RIGHT);
		tabPanel.addTab(givenName, panel);
	}
	
	public JTabbedPane getTabPamel(){
		tabPanel = (JTabbedPane) dto.getComponentList().get("chatTabPanel");
		return tabPanel;
	}
	
	public void removeTabPanel(JPanel panel){
		tabPanel = (JTabbedPane) dto.getComponentList().get("chatTabPanel");
		tabPanel.remove(panel);
	}

	@Override
	public void paint(Graphics g) {
		paintComponents(g);
	}

}
