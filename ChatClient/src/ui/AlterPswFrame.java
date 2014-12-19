package ui;

import java.awt.Graphics;

import javax.swing.JFrame;

import config.FrameConfig;
import control.Control;

/**
 * @author SuperSun
 * Alter password frame
 */
public class AlterPswFrame extends MyFrame{

	/**
	 * consturctor
	 * @param frameCfg frame configuration
	 * @param control control object
	 */
	public AlterPswFrame(FrameConfig frameCfg, Control control) {
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
