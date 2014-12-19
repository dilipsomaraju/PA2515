package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import bean.ContactList;
import bean.Group;
import bean.User;

public class AuthenticationDB {
	//SQL statements
	private static String GET_USER_SQL = "select * from userInfo where userId=?";
	private static String GET_LOGGED_USERS_SQL = "select userInfo.userId,givenName,status,nickName from contactInfo,userInfo " +
			"where contactInfo.userId=? and status='1' and userInfo.userId=contactInfo.userIds;";
	private static String SET_STATUS_SQL = "update userInfo set status=? where userId=?";
	private static String ALTER_PSW_SQL = "update userInfo set psw=? where userId=?";
	private static String INSERT_USER_SQL = "insert into userInfo(userId,psw,nickName" +
			",status,question,answer) VALUES(?,?,?,?,?,?)";
	private static String GET_LOGGED_GROUP_MEMBERS_SQL = "select userIds from groupInfo,userInfo where userId=userIds and status='1' and groupId=?";
	private static String CHECK_USER_RIGHT_SQL = "select right from groupInfo where groupId=? and userId=?";
	private static String GET_GROUP_CONTACT_SQL = "select groupId,nickName,givenName from groupInfo where userIds=?";

/////////////////////////////////////////////////////////////////////////////////////////
	
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
		if(user != null && user.getPsw().equals(psw))
			return true;
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
			stmt.setString(1, psw);
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
	 * Get user's contactList (only logged in users and groups)
	 * @param userId
	 * here the two givenName, one is for friends, one is for groups
	 * User: userId,givenName,status,nickName
	 * group:groupId,nickName,givenName
	 * @return contact list if successed, or return null
	 */
	public static ContactList getContactList(String userId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, GET_LOGGED_USERS_SQL);
		ResultSet rs = null;
		ContactList contactList = new ContactList();
		Vector<User> users = new Vector<User>();
		Vector<Group> groups = new Vector<Group>();
		try {
			stmt.setString(1, userId);
			rs = DB.getRs(stmt);
			while(rs.next()){
				User user = new User();
				user.setUserId(rs.getString(1));
				user.setGivenName(rs.getString(2));
				user.setStatus(rs.getInt(3));
				user.setNickName(rs.getString(4));
				// Only display logged user
				if(user.getStatus() == 1)
					users.add(user);
			}
			stmt = DB.getStmt(conn, GET_GROUP_CONTACT_SQL);
			stmt.setString(1, userId);
			rs = DB.getRs(stmt);
			while(rs.next()){
				Group group = new Group();
				group.setGroupId(rs.getString(1));
				group.setNickName(rs.getString(2));
				group.setGivenName(rs.getString(3));
				groups.add(group);
			}
			contactList.setUsers(users);
			contactList.setGroups(groups);
			return contactList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DB.closeDB(rs, stmt, conn);
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
	 * Get logged users' userId of a group
	 * @param groupId
	 * @return logged groupmembers' userId if successed, or return null
	 */
	public static List<String> getLoggedGroupMemberNames(String groupId){
		List<String> members = new ArrayList<String>();
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, GET_LOGGED_GROUP_MEMBERS_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, groupId);
			rs = DB.getRs(stmt);
			while(rs.next())
				members.add(rs.getString(1));
			return members;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DB.closeDB(rs, stmt, conn);
		}
	}

	/**
	 * Get logged friends' userId of one user
	 * @param userId
	 * @return logged friends' userId if successed, or return null
	 */
	public static List<String> getLoggedFriendNames(String userId){
		List<String> names = new ArrayList<String>();
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, GET_LOGGED_USERS_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, userId);
			rs = DB.getRs(stmt);
			while(rs.next())
				names.add(rs.getString(1));
			return names;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DB.closeDB(rs, stmt, conn);
		}
	}

//	public static void main(String[] args) {
//		System.out.println(isUser(""));
//	}
}
