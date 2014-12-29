package control;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import service.Service;
import dao.AuthenticationDB;
import dto.Dto;

public class Control {
	
	private Service service;
	private Dto dto;
	private TrayIcon trayIcon;
	private Image image;
	private SystemTray tray;
	private MenuItem show;
	private MenuItem exit;
	private PopupMenu pop;
	
	public Control(){
		this.dto = new Dto();
		service = new Service(dto);
		image = new ImageIcon("graphic/tray.png").getImage();
		trayIcon = new TrayIcon(image, "LanChat Server(Closed)");
		pop = new PopupMenu();
		show = new MenuItem("Recover");
		exit = new MenuItem("Exit");
		init();
	}
	
	private void init(){
		final JFrame f = new JFrame();
		JPanel p = new JPanel();
		f.setIconImage(image);
		f.setTitle("Server");
		f.setSize(200, 65);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final JButton start = new JButton("Strat");
		p.add(start);
		f.add(p);
		f.setVisible(true);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(false);
				service.startServer();
				AuthenticationDB.initDB();
				trayIcon.setToolTip("LanChat Server(Started)");
				f.dispose();
				systemTray(f);
			}
		});
		
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				systemTray(f);
			}
		});
	}
	private void systemTray(final JFrame f){
		if(SystemTray.isSupported()){
			tray = SystemTray.getSystemTray();
			trayIcon.setPopupMenu(pop);
			show.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tray.remove(trayIcon);
					f.setVisible(true);
					f.setExtendedState(JFrame.NORMAL);
					f.toFront();
				}
			});
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int i = JOptionPane.showConfirmDialog(null,
							"Sure to close", "Warming",
							JOptionPane.YES_NO_OPTION);
					if(Math.abs(i) == 1);
					else{
						tray.remove(trayIcon);
						System.exit(0);
					}
				}
			});
			pop.add(show);
			pop.add(exit);
			trayIcon.setImageAutoSize(true);
			trayIcon.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) { 
					if (e.getClickCount() == 2) {
						tray.remove(trayIcon);
						f.setVisible(true);
						f.setExtendedState(JFrame.NORMAL);
						f.toFront();
					}
				}
			});
			try {
				tray.add(trayIcon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		}
	}
}
