<%@page import="java.sql.*"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>14-1</title>
</head>
<body>
	<h3>직원등록</h3>
	<form action="14-2.jsp" method="post">
		<table border="1">
			<tr>
				<td>아이디</td>
				<td><input type="text" name="uid"></td>
			</tr>
			<tr>
				<td>이름</td>
				<td><input type="text" name="name"></td>
			</tr>
			<tr>
				<td>전화번호</td>
				<td><input type="text" name="hp" maxlength="13"></td>
			</tr>
			<tr>
				<td>주소</td>
				<td>
					<select name="addr">
						<option>서울</option>
						<option>대전</option>
						<option>대구</option>
						<option>부산</option>
						<option>광주</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>직급</td>
				<td>
					<select name="pos">
						<option>사원</option>
						<option>대리</option>
						<option>과장</option>
						<option>차장</option>
						<option>부장</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>부서</td>
				<td>
					<select name="dep">
						<option value="101">경영지원부</option>
						<option value="102">인사부</option>
						<option value="103">개발부</option>
						<option value="104">영업1부</option>
						<option value="105">영업2부</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right"><input type="submit" value="등록하기"></td>
			</tr>
		</table>
	</form>
	<hr>
<h3>직원등록 내역확인</h3>
	<table border="1">
		<tr>
			<th>순번</th>
			<th>아이디</th>
			<th>이름</th>
			<th>전화번호</th>
			<th>직급</th>
			<th>부서</th>
			<th>부서</th>
			<th>등록일시</th>
		</tr>
<%
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null; 
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
		rs = stmt.executeQuery("select seq, uid, name, hp, addr, pos,"+
									"case dep when 101 then '경영지원부' when 102 then '인사부' when 103 then '개발부' when 104 then '영업1부' when 105 then '영업2부' else '' end as dep, " + 
									"rdate from `USER` order by seq desc");
		//5. 결과셋 처리(select 쿼리)
		while(rs.next()) { 
			Date rdate = new Date(rs.getTimestamp("rdate").getTime());
		%>
		<tr>
			<td><%=rs.getInt("seq") %></td>
			<td><%=rs.getString("uid") %></td>
			<td><%=rs.getString("name") %></td>
			<td><%=rs.getString("hp") %></td>
			<td><%=rs.getString("addr") %></td>
			<td><%=rs.getString("pos") %></td>
			<td><%=rs.getString("dep") %></td>
			<td><%=rdate %></td>
		</tr>
	<%}
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
	</table>

</body>
</html>