<%@page import="kr.co.boardEx.DBConfig"%>
<%@page import="org.json.simple.JSONObject"%>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	String nick = (String)request.getParameter("nick");
	
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	//JSON 데이터 출력용 object (library 파일로부터 인식)
	JSONObject json = new JSONObject();
	int cnt = 0;
	
	//싱글톤 객체 사용
	try (Connection conn = DBConfig.getConnection()){
		pstmt = conn.prepareStatement("SELECT 1 FROM `JSP_MEMBER` WHERE `NICK`=?");
		pstmt.setString(1,nick);
		rs = pstmt.executeQuery();
		if(rs.next()) {
			json.put("result", "이미 존재하는 닉네임입니다.");
			json.put("color", "red");
			out.println(json);
		} else if(nick.length() < 3){
			json.put("result", "닉네임은 최소 3자이상은 입력되어야합니다.");
			json.put("color", "red");
			out.println(json);
		} else if(nick.indexOf(" ") != -1){
			json.put("result", "닉네임에 공백이 포함되어 있습니다.");
			json.put("color", "red");
			out.println(json);
		} else {
			String spc = "{}[]()<>?|`~'!@#$%^&*-+=,.;:\"'\\/ ";
			for(int i = 0; i< spc.length(); i++){
				if(nick.indexOf(spc.charAt(i)) != -1) {
					json.put("result", "닉네임에 특수문자가 포함되어있습니다.");
					json.put("color", "green");
					out.println(json);
					cnt++;
				}
			}
			if(cnt == 0) {
				json.put("result", "사용 가능한 닉네임입니다.");
				json.put("color", "green");
				out.println(json);
			}
		}
		
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		if(rs != null) rs.close();
		if(pstmt != null) pstmt.close();
	}
%>
