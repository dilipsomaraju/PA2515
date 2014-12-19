package util;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;

public class MyList extends JList{
	public MyList(int fontSize){
		this.setFont(new Font("Monaco",Font.BOLD,fontSize));
		((JLabel)this.getCellRenderer()).setOpaque(false);
	}
}
