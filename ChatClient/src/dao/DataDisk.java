package dao;

import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import bean.DiskData;



/**
 * @author SuperSun
 * Implement the interface to access
 * Read or write dto object from or to local disk
 * dto contains User, MSG, HashMap
 */
public class DataDisk implements Data{
	
	private final  String filePath;
	
	/**
	 * Construction method
	 * @param param contains configuration of Disk
	 */
	public DataDisk(HashMap<String,String> param) {
		this.filePath = param.get("path");
	}
	
	@Override
	public DiskData loadData() {
		ObjectInputStream ois = null;
		DiskData dd = new DiskData();
		try {
			//Get the file
			File file = new File(filePath);
			//If file is not exist, create a new one
			if(file.createNewFile()){
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
				oos.writeObject(dd);
			}
			//Get the content of file
			ois = new ObjectInputStream(new FileInputStream(filePath));
			dd = (DiskData) ois.readObject();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return  dd;
	}

	@Override
	public void saveData(DiskData dd) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(dd);
		} catch (Exception e) {
			e.printStackTrace();
		} 	finally{
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
