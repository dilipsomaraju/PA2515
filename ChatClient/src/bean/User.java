package bean;

/**
 * @author SuperSun
 * User Bean
 * 
 */
public class User {
	private String userId;
	private String psw;
	private String nickName;
	private String reName;
	private String status;
	private String question;
	private String answer;
	
	public User() {
	}
	public User(String userId, String psw){
		this.userId = userId;
		this.psw = psw;
	}
	
	public User(String userId, String psw, String nickName, String reName,
			String status, String question, String answer) {
		this.userId = userId;
		this.psw = psw;
		this.nickName = nickName;
		this.reName = reName;
		this.status = status;
		this.question = question;
		this.answer = answer;
	}
	public String getReName() {
		return reName;
	}
	public void setReName(String reName) {
		this.reName = reName;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
}
