package config;

import java.io.Serializable;

import org.dom4j.Element;

/**
 * @author SuperSun
 * Configuration of Components
 */
public class ComponentConfig implements Serializable{
	private String className;
	
	private String path;
	
	private String value;

	private int x;
	
	private int y;

	private int w;
	
	private int h;
	
	/**
	 * Constructor
	 * @param component
	 */
	public ComponentConfig(Element component) {
		this.className = component.attributeValue("className");
		this.path = component.attributeValue("path");
		this.value = component.attributeValue("value");
		this.x = Integer.parseInt(component.attributeValue("x"));
		this.y = Integer.parseInt(component.attributeValue("y"));
		this.w = Integer.parseInt(component.attributeValue("w"));
		this.h = Integer.parseInt(component.attributeValue("h"));
	}

	public String getValue() {
		return value;
	}

	public String getClassName() {
		return className;
	}

	public String getPath() {
		return path;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}
}
