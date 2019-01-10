<%@page import="java.sql.*"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	//전송 파라미터 수신
	String uid = request.getParameter("uid");
	String name = request.getParameter("name");
	String hp = request.getParameter("hp");
	String addr = request.getParameter("addr");
	String pos = request.getParameter("pos");
	int dep = Integer.parseInt(request.getParameter("dep"));
	
	//데이터베이스 작업
	Connection conn = null;
	PreparedStatement pstmt = null;
	final String HOST = "jdbc:mysql://192.168.0.156:3306/jhg?characterEncoding=utf8&serverTimezone=UTC";
	final String USER= "jhg";
	final String PASS = "1234";
	
	try {
		//1. JDBC 드라이버 로드 
		Class.forName("com.mysql.jdbc.Driver");
		//2. 데이터베이스 접속
		conn = DriverManager.getConnection(HOST, USER, PASS);
		//3. 쿼리 실행객체 생성
		pstmt = conn.prepareStatement("insert into `USER` (uid, name, hp, addr, pos, dep, rdate) values (?,?,?,?,?,?,now())");
		pstmt.setString(1, uid);
		pstmt.setString(2, name);
		pstmt.setString(3, hp);
		pstmt.setString(4, addr);
		pstmt.setString(5, pos);
		pstmt.setInt(6, dep);
		//4. 쿼리 실행
		pstmt.executeUpdate();
		//5. 결과셋 처리(select 쿼리)
	} catch(Exception e) {
		e.printStackTrace();
	} finally {
		//6. 데이터베이스 접속 종료
		if(pstmt != null)
			pstmt.close();
		if(conn != null)
			conn.close();
	}
	//redirect
	response.sendRedirect("14-3.jsp");
%>