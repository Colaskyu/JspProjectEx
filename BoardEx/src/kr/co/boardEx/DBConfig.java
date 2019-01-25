package kr.co.boardEx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {

	private final static String HOST = "jdbc:mysql://192.168.0.126:3306/jhg?characterEncoding=utf8&serverTimezone=UTC";
	private final static String USER = "jhg";
	private final static String PASS = "1234";
	
	//教臂沛 按眉 积己 static栏肺 积己
	public static Connection getConnection() throws Exception {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(HOST, USER, PASS);
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
