package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatDB {
	private static String GET_LOGGED_GROUP_MEMBERS_SQL = "select userIds from groupInfo,userInfo where userId=userIds and status='1' and groupId=?";
	private static String GET_LOGGED_USERS_SQL = "select userInfo.userId,givenName,status,nickName from contactInfo,userInfo " +
	"where contactInfo.userId=? and status='1' and userInfo.userId=contactInfo.userIds;";
	
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
}
