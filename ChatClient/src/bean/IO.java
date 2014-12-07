package bean;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IO {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public IO() {
	}
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
