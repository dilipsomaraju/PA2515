package control;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

import service.ClientService;
import ui.MyFrame;
import config.DataInterfaceConfig;
import config.FrameConfig;
import config.RootConfig;
import config.UIConfig;
import dao.Data;
import dao.SocketCommunicate;
import dto.Dto;

/**
 * @author SuperSun
 * Control layer
 */
public class Control {
	private Dto dto;
	
	private final Data dataDisk;

	private ClientService clientService;

	/**
	 * Constructor
	 */
	public Control() {
		dto = new Dto(this);
		//Get local disk data
		this.dataDisk = this.creatDataObject(RootConfig.getDataConfig().getDiskData());
		this.dto.setDiskData(dataDisk.loadData());
		//Create SocketCommunicate object
		dto.setSc(new SocketCommunicate());
		clientService = new ClientService(dto,this);
		//init Frames
		initFrame(dto);
		//Get initial data in disk
		clientService.initComponents();
	}

	/**
	 * initialize all frames, using their own paint method 
	 */
	private void initFrame(Dto dto) {
		try {
			// Get UI config
			UIConfig fCfg = RootConfig.getUIConfig();
			// Get Frames config
			List<FrameConfig> framesCfg = fCfg.getFramesConfig();
			
			// Create frame based on its configuration data
			for (FrameConfig frameCfg : framesCfg) {
				//reflect use className get the Class
				Class<?> cls = Class.forName(frameCfg.getClassName());
				// Get Constructor
				Constructor<?> ctr = cls.getConstructor(FrameConfig.class, control.Control.class);
				// Create objec based on consturctor
				MyFrame f = (MyFrame) ctr.newInstance(frameCfg, this);
				if(frameCfg.getClassName().equals("ui.MainFrame"))
					dto.getFrameList().put("MainFrame", f);
				if(frameCfg.getClassName().equals("ui.ChatFrame"))
					dto.getFrameList().put("ChatFrame", f);
				if(frameCfg.getClassName().equals("ui.ConnectFrame"))
					dto.getFrameList().put("ConnectFrame", f);
				if(frameCfg.getClassName().equals("ui.LoginFrame"))
					dto.getFrameList().put("LoginFrame", f);
				if(frameCfg.getClassName().equals("ui.RegisterFrame"))
					dto.getFrameList().put("RegisterFrame", f);
				if(frameCfg.getClassName().equals("ui.AlterPswFrame"))
					dto.getFrameList().put("AlterPswFrame", f);
				if(frameCfg.getClassName().equals("ui.ConfigFrame"))
					dto.getFrameList().put("ConfigFrame", f);
				if(frameCfg.getClassName().equals("ui.FindFrame"))
					dto.getFrameList().put("FindFrame", f);
				if(frameCfg.getClassName().equals("ui.InviteToGroupFrame"))
					dto.getFrameList().put("InviteToGroupFrame", f);
				f.setDto(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param cfg DataInterfaceConfig
	 * @return Interface for disk data access
	 */
	private Data creatDataObject(DataInterfaceConfig cfg){
		try {
			//Get Class
			Class<?> cls = Class.forName(cfg.getClassName()) ;
			//GetConstructor
			Constructor<?> ctr = cls.getConstructor(HashMap.class);
			//Create object
			ctr.newInstance(cfg.getParam());
			return (Data)ctr.newInstance(cfg.getParam());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

	public Data getDiskData() {
		return dataDisk;
	}

	public Dto getDto() {
		return dto;
	}

	public ClientService getClientService() {
		return clientService;
	}
}
