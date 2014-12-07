package ui;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * @author SuperSun
 * Graphics Class, Save all graphics
 */
public class Img {
	
	private Img(){}
	
	public static Image getImg(String path){
		Image img = new ImageIcon("graphic/"+path).getImage();
		return img;
	}

	public static ImageIcon getImgIcon(String path) {
		ImageIcon img = new ImageIcon("graphic/"+path);
		return img;
	}
}
