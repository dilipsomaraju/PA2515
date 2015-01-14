package bean;

import java.io.Serializable;

/**
 * @author SuperSun
 * User Bean
 */
public class User implements Serializable{
	private String userId;
	private String psw;
	private String nickName;
	private String givenName;
	private int status;
	private String question;
	private String answer;
	
	/**
	 * Constructor
	 */
	public User() {
	}
	
	/**
	 * Constructor
	 * @param userId
	 * @param psw
	 */
	public User(String userId, String psw){
		this.userId = userId;
		this.psw = psw;
	}
	
	/**
	 * Constructor
	 * @param userId
	 * @param psw
	 * @param nickName
	 * @param reName
	 * @param status
	 * @param question
	 * @param answer
	 */
	public User(String userId, String psw, String nickName, String reName,
			int status, String question, String answer) {
		this.userId = userId;
		this.psw = psw;
		this.nickName = nickName;
		this.givenName = reName;
		this.status = status;
		this.question = question;
		this.answer = answer;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String reName) {
		this.givenName = reName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String username) {
		this.userId = username;
	}
	public String getPsw() {
		return psw;
	}
	public void setPsw(String psw) {
		this.psw = psw;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String toString(){
		return givenName+"  ("+nickName+")";
	}
}
