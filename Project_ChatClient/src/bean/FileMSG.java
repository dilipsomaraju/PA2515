package bean;

import java.io.File;

/**
 * @author SuperSun
 * Extend MSG. Used when send message to Server
 * file is the file message object
 * receiverId the file message's destination
 */
public class FileMSG extends MSG{
	private String receiverId;
	private File file;
	
	
	/**
	 * Constructor
	 */
	public FileMSG() {
		super();
	}
	
	/**
	 * Constructor
	 * @param senderId
	 * @param toM
	 * @param receiverId
	 * @param file
	 */
	public FileMSG(String senderId, String toM, String receiverId, File file) {
		super(senderId, toM);
		this.receiverId = receiverId;
		this.file = file;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
}
