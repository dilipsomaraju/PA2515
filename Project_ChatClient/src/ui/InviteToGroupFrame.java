package ui;

import java.awt.Graphics;

import javax.swing.JFrame;

import config.FrameConfig;
import control.Control;

public class InviteToGroupFrame extends MyFrame{

	public InviteToGroupFrame(FrameConfig frameCfg, Control control){
		super(frameCfg, control);
		this.createFrame();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void paint(Graphics g) {
		paintComponents(g);
	}
}
