package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ContactList;
import bean.Group;
import bean.User;

public class AuthenticationDB {
	private static String CHECK_USER_SQL = "select * from userInfo where userId=?";
	private static String CHECK_LOGIN_SQL = "select * from userInfo where userId=? and psw=?";
	private static String SET_STATUS_SQL = "update userInfo set status=? where userId=?";
	private static String CHANGE_PSW_SQL = "update userInfo set psw=? where userId=?";
	private static String INSERT_USER_SQL = "insert into userInfo(userId,psw,nickName" +
			",status,question,answer) VALUES(?,?,?,?,?,?)";

	private static String GET_CONTACT_SQL = "select userIds,givenName,status" +
			" from userInfo,contactInfo where contactInfo.userId=userInfo.userId and contactInfo.userIds<>?";

	private static String GET_GROUP_CONTACT_SQL = "select groupId,nickName,givenName from groupInfo where userIds=?";
	
	private static String GET_GROUP_MEMBERS_SQL = "select userIds from groupInfo where groupId=?";
	
	private static String CHECK_USER_RIGHT_SQL = "select right from groupInfo where groupId=? and userId=?";

	public static boolean isUser(String userId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, CHECK_USER_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, userId);
			rs = DB.getRs(stmt);
			if(rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(rs, stmt, conn);
		}
		return false;
	}
	
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

	public static boolean checkLogin(String userId, String psw){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, CHECK_LOGIN_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, userId);
			stmt.setString(2, psw);
			rs = DB.getRs(stmt);
			if(rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(rs, stmt, conn);
		}
		return false;
	}

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

	public static boolean changePsw(String userId, String psw){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, CHANGE_PSW_SQL);
		try {
			stmt.setString(1, userId);
			stmt.setString(2, psw);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(stmt, conn);
		}
	}

	public static ContactList getContactList(String userId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, GET_CONTACT_SQL);
		ResultSet rs = null;
		ContactList contactList = new ContactList();
		List<User> users = new ArrayList<User>();
		List<Group> groups = new ArrayList<Group>();
		try {
			stmt.setString(1, userId);
			rs = DB.getRs(stmt);
			while(rs.next()){
				User user = new User();
				user.setUserId(rs.getString(1));
				user.setNickName(rs.getString(2));
				user.setStatus(rs.getInt(3));
				users.add(user);
			}
			stmt = DB.getStmt(conn, GET_GROUP_CONTACT_SQL);
			stmt.setString(1, userId);
			rs = DB.getRs(stmt);
			while(rs.next()){
				Group group = new Group();
				group.setGroupId(rs.getString(1));
				group.setNickName(rs.getString(2));
				group.setReName(rs.getString(3));
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
	
	public static List<String> getGroupMembers(String groupId){
		List<String> members = new ArrayList<String>();
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, GET_GROUP_MEMBERS_SQL);
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

	public static boolean checkUserRight(String groupId, String userId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, CHECK_USER_RIGHT_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, groupId);
			stmt.setString(2, userId);
			rs = DB.getRs(stmt);
			if(rs.getInt(1) == 0)
				return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(rs, stmt, conn);
		}
	}

	
//	public static void main(String[] args) {
//		System.out.println(isUser(""));
//	}
}
