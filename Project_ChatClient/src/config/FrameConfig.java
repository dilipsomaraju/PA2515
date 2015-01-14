package config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

/**
 * @author SuperSun
 * Configuration of Frame
 */
public class FrameConfig implements Serializable{
	/**
	 * Frame title
	 */
	private final String title;
	
	/**
	 * Frame graphic's path
	 */
	private final String path;
	
	/**
	 * Frame width 
	 */
	private final int w;
	
	/**
	 * Frame height
	 */
	private final int h;
	
	/**
	 * Frame point.x of left-top corner
	 */
	private final int x;
	
	/**
	 * Frame point.y of left-top corner
	 */
	private final int y;

	/**
	 * Class Name
	 */
	private final String className;
	
	/**
	 * Frame style
	 */
	private final int frameStyle;
	
	private final List<ComponentConfig> componentsConfig;
	
	/**
	 * Constructor
	 * @param frame
	 */
	public FrameConfig(Element frame){
		@SuppressWarnings("unchecked")
		List<Element> components = frame.elements("component");
		componentsConfig = new ArrayList<ComponentConfig>();
		for (Element component : components) {
			//Get a component's attributes
			ComponentConfig cc = new ComponentConfig(component);
			componentsConfig.add(cc);
		}
		//Get elements of a frame
		this.className = frame.attributeValue("className");
		this.title = frame.attributeValue("title");
		this.path = frame.attributeValue("path");
		this.x = Integer.parseInt(frame.attributeValue("x"));
		this.y = Integer.parseInt(frame.attributeValue("y"));
		this.w = Integer.parseInt(frame.attributeValue("w"));
		this.h = Integer.parseInt(frame.attributeValue("h"));
		this.frameStyle = Integer.parseInt(frame.attributeValue("frameStyle"));
	}

	public List<ComponentConfig> getComponentsConfig() {
		return componentsConfig;
	}

	public String getTitle() {
		return title;
	}
	public String getPath() {
		return path;
	}
	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getClassName() {
		return className;
	}

	public int getFrameStyle() {
		return frameStyle;
	}
}
