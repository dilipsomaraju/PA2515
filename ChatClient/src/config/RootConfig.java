package config;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class RootConfig {
	
	private static UIConfig UI_CONFIG = null;
	
	private static DataConfig DATA_CONFIG = null;

	private static final boolean IS_DEBUG = true;
	
	static {
		try {
			//The way to read configuration file when do debugging
			if(IS_DEBUG){
			// Create XML reader
			SAXReader reader = new SAXReader();
			// Get XML file
			Document doc = reader.read("data/cfg.xml");
			// Get the root node
			Element root = doc.getRootElement();
			//Get UI configuration
			UI_CONFIG = new UIConfig(root.element("ui"));
			//Get Data Accessing configuration
			DATA_CONFIG = new DataConfig(root.element("data"));
			}
			//The method to reading configuration after releasing software
			else{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/uiCfg.dat"));
				UI_CONFIG = (UIConfig)ois.readObject();
				ois = new ObjectInputStream(new FileInputStream("data/dataCfg.dat"));
				DATA_CONFIG = (DataConfig)ois.readObject();
				ois.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Construction method, do not allowed to create object outside
	 */
	private RootConfig(){}
	
	/**
	 * return data config
	 */
	public static DataConfig getDataConfig(){
		return DATA_CONFIG;
	}
	
	/**
	 * retrun UI config
	 */
	public static UIConfig getUIConfig() {
		return UI_CONFIG;
	}
// After debugging use this code to renew those config.dat 	
//	public static void main(String[] args) throws Exception {
//		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/uiCfg.dat"));
//		oos.writeObject(UI_CONFIG);
//		oos = new ObjectOutputStream(new FileOutputStream("data/dataCfg.dat"));
//		oos.writeObject(DATA_CONFIG);
//	}
}
