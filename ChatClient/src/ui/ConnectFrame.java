package ui;

import java.awt.Graphics;

import javax.swing.JFrame;

import config.FrameConfig;
import control.Control;

public class ConnectFrame extends MyFrame {

	public ConnectFrame(FrameConfig frameCfg, Control control) {
		super(frameCfg, control);
		this.createFrame();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void paint(Graphics g) {
		paintComponents(g);
	}

}
