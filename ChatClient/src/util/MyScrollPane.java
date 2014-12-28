package util;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class MyScrollPane extends JScrollPane{
	public MyScrollPane(JComponent c){
		super(c,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setWheelScrollingEnabled(true);
		c.setOpaque(false);
		this.setOpaque(false); 
		this.getViewport().setOpaque(false);
	}
	
	public MyScrollPane(JComponent c, String s){
		super(c,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setWheelScrollingEnabled(true);
		c.setOpaque(false);
		this.setOpaque(false); 
		this.getViewport().setOpaque(false);
	}
}
