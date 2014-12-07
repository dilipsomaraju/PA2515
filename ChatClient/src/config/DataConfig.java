package config;

import java.io.Serializable;

import org.dom4j.Element;

public class DataConfig implements Serializable{
	
	private final DataInterfaceConfig diskData;
	
	public DataConfig(Element data) {
		this.diskData = new DataInterfaceConfig(data.element("diskData"));
	}

	public DataInterfaceConfig getDiskData() {
		return diskData;
	}
}
