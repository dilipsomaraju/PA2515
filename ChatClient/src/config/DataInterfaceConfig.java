package config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

public class DataInterfaceConfig  implements Serializable{
	/**
	 * Class name
	 */
	private final String className;
	
	/**
	 * Hask map
	 */
	private final HashMap<String,String> param;
	
	public DataInterfaceConfig (Element dataInterfaceConfig){
		//Get Class Name
		this.className = dataInterfaceConfig.attributeValue("className");
		this.param = new HashMap<String,String>();
		// Use Hashmap to read XML file
		@SuppressWarnings("unchecked")
		List<Element> params = dataInterfaceConfig.elements("param"); 
		for (Element e : params) {
			// insert Key - Value into hash map 
			this.param.put(e.attributeValue("key"), e.attributeValue("value"));
		}
	}
	public String getClassName() {
		return className;
	}
	public HashMap<String, String> getParam() {
		return param;
	}
}
