<%@page import="org.json.simple.JSONObject"%>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	String uid = (String)request.getParameter("uid");
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	final String HOST = "jdbc:mysql://192.168.0.126:3306/jhg?characterEncoding=utf8&serverTimezone=UTC";
	final String DBUser = "jhg";
	final String DBPw = "1234";
	
	//JSON 데이터 출력용 object (library 파일로부터 인식)
	JSONObject json = new JSONObject();
	
	try {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(HOST, DBUser, DBPw);
		pstmt = conn.prepareStatement("SELECT 1 FROM `JSP_MEMBER` WHERE `UID`=?");
		pstmt.setString(1,uid);
		rs = pstmt.executeQuery();
		if(rs.next()) {
			json.put("result", "이미 존재하는 아이디입니다.");
			json.put("color", "red");
			out.println(json);
		} else if(uid.length() < 4){
			json.put("result", "아이디는 최소 4자이상은 입력되어야합니다..");
			json.put("color", "red");
			out.println(json);
		} else {
			json.put("result", "사용 가능한 아이디입니다.");
			json.put("color", "green");
			out.println(json);
		}
		
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		if(rs != null) rs.close();
		if(pstmt != null) pstmt.close();
		if(conn != null) conn.close();
	}
%>
