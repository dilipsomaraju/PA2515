package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bean.User;

public class AuthenticationDB {
	//SQL statements
	private static String GET_USER_SQL = "select * from userInfo where userId=?";
	private static String INIT_DB_SQL = "update userInfo set status='0'";
	private static String SET_STATUS_SQL = "update userInfo set status=? where userId=?";
	private static String ALTER_PSW_SQL = "update userInfo set psw=? where userId=?";
	private static String INSERT_USER_SQL = "insert into userInfo(userId,psw,nickName" +
			",status,question,answer) VALUES(?,?,?,?,?,?)";
	private static String CHECK_USER_RIGHT_SQL = "select right from groupInfo where groupId=? and userId=?";

/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Initialize Database when start Server
	 */
	public static void initDB(){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, INIT_DB_SQL);
		try {
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeDB(stmt, conn);
		}
	}
	
	/**
	 * Get all attributes of a user whose id is userId
	 * @param userId
	 * @return User with attributes of userId,psw,nickName,status,question,answer if successed,
	 * or return null
	 */
	public static User getUser(String userId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, GET_USER_SQL);
		ResultSet rs = null;
		User user = new User();
		try {
			stmt.setString(1, userId);
			rs = DB.getRs(stmt);
			if(rs.next()){
				user.setUserId(userId);
				user.setPsw(rs.getString(2));
				user.setNickName(rs.getString(3));
				user.setStatus(rs.getInt(4));
				user.setQuestion(rs.getString(5));
				user.setAnswer(rs.getString(6));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DB.closeDB(rs, stmt, conn);
		}
		return null;
	}
	
	/**
	 * Judge whether this ID is existed or not
	 * @param userId
	 * @return isUser return true, or return false
	 */
	public static boolean isUser(String userId){
		if(getUser(userId) != null)
			return true;
		return false;
	}
	
	/**
	 * Insert a new user into userInfo
	 * @param userId
	 * @param psw
	 * @param nickName
	 * @param question
	 * @param answer
	 * @return true if successed, or reture false
	 */
	public static boolean insertUser(String userId, String psw, String nickName, String question, String answer){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, INSERT_USER_SQL);
		try {
			stmt.setString(1, userId);
			stmt.setString(2, psw);
			stmt.setString(3, nickName);
			stmt.setInt(4, 0);
			stmt.setString(5, question);
			stmt.setString(6, answer);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(stmt, conn);
		}
	}

	/**
	 * Check whether the psw is correct
	 * @param userId
	 * @param psw
	 * @return true if correct, or return false
	 */
	public static boolean checkLogin(String userId, String psw){
		User user = getUser(userId);
		if(user != null && user.getPsw().equals(psw) && user.getStatus() == 0){
			setStatus(userId, 1);
			return true;
		}
		return false;
	}

	/**
	 * Set user's status whose id is userId
	 * @param userId
	 * @param status
	 * @return true if successed, or return false
	 */
	public static boolean setStatus(String userId, int status){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, SET_STATUS_SQL);
		try {
			stmt.setInt(1, status);
			stmt.setString(2, userId);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(stmt, conn);
		}
	}

	/**
	 * Alter password 
	 * @param userId
	 * @param psw
	 * @return true if successed, or return false
	 */
	public static boolean alterPsw(String userId, String psw){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, ALTER_PSW_SQL);
		try {
			stmt.setString(2, userId);
			stmt.setString(1, psw);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(stmt, conn);
		}
	}

	/**
	 * Check whether this user has right to edit this group
	 * @param groupId
	 * @param userId
	 * @return true if it has right, or return false
	 */
	public static boolean checkUserRight(String groupId, String userId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, CHECK_USER_RIGHT_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, groupId);
			stmt.setString(2, userId);
			rs = DB.getRs(stmt);
			if(rs.getInt(1) == 1)
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(rs, stmt, conn);
		}
	}

	/**
	 * @param userId
	 * @return userNickName
	 */
	public static String getUserNickName(String userId){
		User user = getUser(userId);
		if(user != null)
			return user.getNickName();
		return "";
	}

//	public static void main(String[] args) {
//		System.out.println(isUser(""));
//	}
}
