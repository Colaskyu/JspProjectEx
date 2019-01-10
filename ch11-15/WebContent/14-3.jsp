<%@page import="sub1.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.*"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>14-3</title>
</head>
<body>
<%
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null; 
	ArrayList<User> list = new ArrayList<>();;
	final String HOST = "jdbc:mysql://192.168.0.156:3306/jhg?characterEncoding=utf8&serverTimezone=UTC";
	final String USER= "jhg";
	final String PASS = "1234";
	try {
		//1. JDBC 드라이버 로드 
		Class.forName("com.mysql.jdbc.Driver");
		//2. 데이터베이스 접속
		conn = DriverManager.getConnection(HOST, USER, PASS);
		//3. 쿼리 실행객체 생성
		stmt = conn.createStatement();
		//4. 쿼리 실행
		rs = stmt.executeQuery("select seq, uid, name, hp, addr, pos, dep, rdate from `USER` order by seq desc");
		//5. 결과셋 처리(select 쿼리)
		while(rs.next()) { 
			User user = new User();
			user.setSeq(rs.getInt(1));
			user.setUId(rs.getString(2));
			user.setName(rs.getString(3));
			user.setHp(rs.getString(4));
			user.setAddr(rs.getString(5));
			user.setPos(rs.getString(6));
			user.setDep(rs.getInt(7));
			user.setRDate(rs.getString(8));
			list.add(user);
		}
	} catch(Exception e) {
		e.printStackTrace();
	} finally {
		//6. 데이터베이스 접속 종료
		if(rs != null)
			rs.close();
		if(stmt != null)
			stmt.close();
		if(conn != null)
			conn.close();
	}
%>

	<h3>직원목록</h3>
	<table border="1">
		<tr>
			<th>순번</th>
			<th>아이디</th>
			<th>이름</th>
			<th>전화번호</th>
			<th>주소</th>
			<th>직급</th>
			<th>부서</th>
			<th>등록일시</th>
			<th>수정</th>
			<th>삭제</th>
		</tr>
	<% for(User a:list) { 	%>
		<tr>
			<td><%=a.getSeq() %></td>
			<td><%=a.getUId() %></td>
			<td><%=a.getName() %></td>
			<td><%=a.getHp() %></td>
			<td><%=a.getAddr() %></td>
			<td><%=a.getPos() %></td>
			<td><%=a.getDep() %></td>
			<td><%=a.getRDate() %></td>
			<td><a href="14-5.jsp?seq=<%=a.getSeq()%>">수정</a></td>
			<td><a href="14-4.jsp?seq=<%=a.getSeq()%>">삭제</a></td>
		</tr>
	<% } %>
	</table>
</body>
</html>