package bean;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author SuperSun
 * A package of ObjectOutputStream and ObjectInputStream
 */
public class IO {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	/**
	 * Constructor
	 * @param oos ObjectOutputStream
	 * @param ois ObjectInputStream
	 */
	public IO(ObjectOutputStream oos, ObjectInputStream ois) {
		this.oos = oos;
		this.ois = ois;
	}
	public ObjectOutputStream getOos() {
		return oos;
	}
	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}
	public ObjectInputStream getOis() {
		return ois;
	}
	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}
}
