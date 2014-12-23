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

public class ContactListDB {
	private static String GET_LOGGED_USERS_SQL = "select userInfo.userId,givenName,status,nickName from contactInfo,userInfo " +
	"where contactInfo.userId=? and status='1' and userInfo.userId=contactInfo.userIds;";
	private static String GET_GROUP_CONTACT_SQL = "select groupId,nickName,givenName from groupInfo where userIds=?";
	private static String GET_GROUP_NICKNAME_SQL = "select distinct nickName from groupInfo where groupId=?";
	private static String ADD_GROUP_SQL = "insert into groupInfo VALUES(?,?,?,?,?)";
	private static String ADD_FRIEND_SQL = "insert into contactInfo VALUES(?,?,?)";
	private static String RENAME_FRIEND_SQL = "update contactInfo set givenName=? where userId=? and userIds=?";
	private static String RENAME_GROUP_SQL = "update groupInfo set givenName=? where groupId=? and userIds=?";
	private static String DELETE_FRIEND_SQL = "delete from contactInfo where userId=? and userIds=?";
	private static String DELETE_GROUP_SQL = "delete from groupInfo where groupId=? and userIds=?";
	private static String HAS_FRIEND_SQL = "select * from contactInfo where userId=? and userIds=?";
	private static String HAS_GROUP_SQL = "select * from groupInfo where groupId=? and userIds=?";
	private static String GET_GROUP_SQL = "select * from groupInfo where groupId=?";
	private static String FIND_SQL = "select ? from ? where ? like '%?%'";
	
	
	
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
	 * Add friend NOTICE: it is a bi-operation
	 * @param userId
	 * @param friendId
	 * @param givenName
	 * @return ture if successfully, or return false
	 */
	public static boolean addFriend(String userId, String friendId, String givenName){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, ADD_FRIEND_SQL);
		try {
			stmt.setString(1, userId);
			stmt.setString(2, friendId);
			stmt.setString(3, givenName);
			stmt.addBatch();
			stmt.setString(1, friendId);
			stmt.setString(2, userId);
			stmt.setString(3, AuthenticationDB.getUser(userId).getNickName());
			stmt.addBatch();
			stmt.executeBatch();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(stmt, conn);
		}
	}
	
	/**
	 * REName a friend
	 * @param userId
	 * @param friendId
	 * @param givenName
	 * @return ture if successfully, or return false
	 */
	public static boolean reNameFriend(String userId, String friendId, String givenName){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, RENAME_FRIEND_SQL);
		try {
			stmt.setString(1, givenName);
			stmt.setString(2, userId);
			stmt.setString(3, friendId);
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
	 * Delete friend NOTICE: Bi-operation
	 * @param userId
	 * @param fiendId
	 * @return ture if successfully, or return false
	 */
	public static boolean deleteFirend(String userId, String fiendId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, DELETE_FRIEND_SQL);
		try {
			stmt.setString(1, userId);
			stmt.setString(2, fiendId);
			stmt.addBatch();
			stmt.setString(1, fiendId);
			stmt.setString(2, userId);
			stmt.addBatch();
			stmt.executeBatch();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(stmt, conn);
		}
	}
	
	/**
	 * Judge whether this group with certain groupId exist or not
	 * @param groupId
	 * @return ture if exist, or return false
	 */
	public static boolean isGroup(String groupId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, GET_GROUP_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, groupId);
			rs = DB.getRs(stmt);
			if(rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			DB.closeDB(stmt, conn);
		}
		return false;
	}
	
	/**
	 * Create a group right = 1 and Name the nickName
	 * @param groupId
	 * @param nickName
	 * @param userId
	 * @param givenName
	 * @return ture if successfully, or return false
	 */
	public static boolean createGroup(String groupId, String nickName, String userId, String givenName){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, ADD_GROUP_SQL);
		try {
			stmt.setString(1, groupId);
			stmt.setString(2, nickName);
			stmt.setString(3, userId);
			stmt.setString(4, givenName);
			stmt.setInt(5, 1);
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
	 * Add a group right = 0 and No nickName
	 * @param groupId
	 * @param userId
	 * @param givenName
	 * @return
	 */
	public static boolean addGroup(String groupId, String userId, String givenName){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, ADD_GROUP_SQL);
		try {
			stmt.setString(1, groupId);
			stmt.setString(2, getGroupNickName(groupId));
			stmt.setString(3, userId);
			stmt.setString(4, givenName);
			stmt.setInt(5, 0);
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
	 * ReName a group
	 * @param groupId
	 * @param userId
	 * @param givenName
	 * @return ture if successfully, or return false
	 */
	public static boolean reNameGroup(String groupId, String userId, String givenName){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, RENAME_GROUP_SQL);
		try {
			stmt.setString(1, givenName);
			stmt.setString(2, groupId);
			stmt.setString(3, userId);
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
	 * Delete a group
	 * @param grouId
	 * @param userId
	 * @return ture if successfully, or return false
	 */
	public static boolean deleteGroup(String groupId, String userId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, DELETE_GROUP_SQL);
		try {
			stmt.setString(1, groupId);
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
	 * user Parameters to consist the Search SQL
	 * @param clue Client input clue
	 * @param tableName userInfo/groupInfo
	 * @param id userId/groupId
	 * @param type located attribute
	 * @return id list 
	 */
	public static List<String> find(String id, String tableName, String attibute, String clue){
		List<String> ids = new ArrayList<String>();
		Connection conn = DB.getConn();
		PreparedStatement stmt = null;
		if(tableName.equals("groupInfo"))
			stmt = DB.getStmt(conn,FIND_SQL);
		else if(tableName.equals("userInfo"))
			stmt = DB.getStmt(conn, FIND_SQL+" and status='1'");
		ResultSet rs = null;
		try {
			stmt.setString(1, id);
			stmt.setString(2, tableName);
			stmt.setString(3, attibute);
			stmt.setString(4, clue);
			rs = DB.getRs(stmt);
			while(rs.next()){
				ids.add(rs.getString(1));
			}
			return ids;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get group's nick name
	 * @param groupId
	 * @return group's nick name if this group exist, or return null
	 */
	public static String getGroupNickName(String groupId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, GET_GROUP_NICKNAME_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, groupId);
			rs = DB.getRs(stmt);
			if(rs.next()){
				return rs.getString(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			DB.closeDB(rs, stmt, conn);
		}
		return null;
	}
	
	/**
	 * Judge whether add this friend before
	 * @param userId
	 * @param FriendId
	 * @return true if has this friend, or return false
	 */
	public static boolean hasThisFriend(String userId, String FriendId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, HAS_FRIEND_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, userId);
			stmt.setString(2, FriendId);
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
	
	/**
	 * Judge whether add this group before
	 * @param userId
	 * @param groupId
	 * @return true if has this group, or return false
	 */
	public static boolean hashThisGroup(String userId, String groupId){
		Connection conn = DB.getConn();
		PreparedStatement stmt = DB.getStmt(conn, HAS_GROUP_SQL);
		ResultSet rs = null;
		try {
			stmt.setString(1, groupId);
			stmt.setString(2, userId);
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
}
