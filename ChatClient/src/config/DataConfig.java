package config;

import java.io.Serializable;

import org.dom4j.Element;

/**
 * @author SuperSun
 * Configuration of disk data
 */
public class DataConfig implements Serializable{
	private final DataInterfaceConfig diskData;
	
	/**
	 * Constructor
	 * @param data
	 */
	public DataConfig(Element data) {
		this.diskData = new DataInterfaceConfig(data.element("diskData"));
	}

	public DataInterfaceConfig getDiskData() {
		return diskData;
	}
}
