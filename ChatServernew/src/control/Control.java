package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import service.Service;
import dto.Dto;

public class Control {
	
	private Service service;
	private Dto dto;
	
	public Control(){
		this.dto = new Dto();
		service = new Service(dto);
		init();
	}
	
	private void init(){
		JFrame f = new JFrame();
		JPanel p = new JPanel();
		f.setTitle("Server");
		f.setSize(200, 65);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		final JButton start = new JButton("Strat");
		p.add(start);
		f.add(p);
		f.setVisible(true);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(false);
				service.startServer();
			}
		});
		
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(JOptionPane.showConfirmDialog(null,"Sure to Close?","Exit",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
					System.exit(0);
			}

		});
	}
}
