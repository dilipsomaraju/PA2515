package dto;

import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Dto {
	private HashMap<String, ObjectOutputStream> userOutput;
	
	public Dto(){
		userOutput = new HashMap<String, ObjectOutputStream>();
	}
	
	public HashMap<String, ObjectOutputStream> getUserOutput() {
		return userOutput;
	}

	public void setUserOutput(HashMap<String, ObjectOutputStream> userOutput) {
		this.userOutput = userOutput;
	}

	
	
	
}
