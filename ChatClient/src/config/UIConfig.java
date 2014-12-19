package config;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

/**
 * @author SuperSun
 * Configuration of UI
 */
public class UIConfig {
	private List<FrameConfig> framesConfig;
	
	/**
	 * Constructor
	 * @param ui
	 */
	public UIConfig(Element ui) {
		@SuppressWarnings("unchecked")
		List<Element> frames = ui.elements("frame");
		framesConfig = new ArrayList<FrameConfig>();
		for (Element frame : frames) {
			//Set a frame's attributes
			FrameConfig fc = new FrameConfig(frame);
			framesConfig.add(fc);
		}
	}

	public List<FrameConfig> getFramesConfig() {
		return framesConfig;
	}
	
}
