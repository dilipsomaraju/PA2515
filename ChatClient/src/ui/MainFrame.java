package ui;

import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import config.FrameConfig;
import control.Control;

public class MainFrame extends MyFrame{
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
	}

	@Override
	public void paint(Graphics g) {
		paintComponents(g);
	}
}
