<%@page import="org.json.simple.JSONObject"%>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	String email = (String)request.getParameter("email");
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	final String HOST = "jdbc:mysql://192.168.0.126:3306/jhg?characterEncoding=utf8&serverTimezone=UTC";
	final String DBUser = "jhg";
	final String DBPw = "1234";
	int cnt = 0;
	//JSON 데이터 출력용 object (library 파일로부터 인식)
	JSONObject json = new JSONObject();
	
	try {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(HOST, DBUser, DBPw);
		pstmt = conn.prepareStatement("SELECT 1 FROM `JSP_MEMBER` WHERE `EMAIL`=?");
		pstmt.setString(1,email);
		rs = pstmt.executeQuery();
		if(rs.next()) {
			json.put("result", "이미 존재하는 이메일입니다.");
			json.put("color", "red");
			out.println(json);
		} else if(email == ""){
			json.put("result", "이메일을 입력해주세요.");
			json.put("color", "red");
			out.println(json);
		} else if(email.indexOf("@") == -1){
			json.put("result", "입력하신 메일주소가 형식에 맞지않습니다.");
			json.put("color", "red");
			out.println(json);
		} else {
			json.put("result", "사용 가능한 이메일입니다.");
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
