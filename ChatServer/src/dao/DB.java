package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {
	public static Connection getConn(){
		Connection conn = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			//TODO
			conn = DriverManager.getConnection("jdbc:mysql://localhost/online_ordering_system?user=root&password=123");
		}	catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static PreparedStatement getStmt(Connection conn, String sql) {
		PreparedStatement stmt = null;
		try {
			if(conn != null) {
				stmt = conn.prepareStatement(sql);
			}
		}	catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}
	
	public static ResultSet getRs(PreparedStatement stmt) {
		ResultSet rs = null;
		try {
			if(stmt != null) {
				rs = stmt.executeQuery();
			}
		}	catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void closeConn(Connection conn) {
		try{
			if(conn != null) {
				conn.close();
				conn = null;
			}
		}	catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeStmt(PreparedStatement stmt) {
		try{
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
		}	catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeRs(ResultSet rs) {
		try{
			if(rs != null) {
				rs.close();
				rs = null;
			}
		}	catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void closeDB(ResultSet rs, PreparedStatement stmt, Connection conn)	{
		closeRs(rs);
		closeStmt(stmt);
		closeConn(conn);
	}
	public static void closeDB(PreparedStatement stmt, Connection conn)	{
		closeStmt(stmt);
		closeConn(conn);
	}
}
