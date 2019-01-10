<%@page import="java.sql.*"%>
<%@page import="sub1.User"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null; 
	User user = new User();
	final String HOST = "jdbc:mysql://192.168.0.156:3306/jhg?characterEncoding=utf8&serverTimezone=UTC";
	final String USER= "jhg";
	final String PASS = "1234";
	int seq = Integer.parseInt(request.getParameter("seq"));
	try {
		//1. JDBC 드라이버 로드 
		Class.forName("com.mysql.jdbc.Driver");
		//2. 데이터베이스 접속
		conn = DriverManager.getConnection(HOST, USER, PASS);
		//3. 쿼리 실행객체 생성
		pstmt = conn.prepareStatement("select seq, uid, name, hp, addr, pos, dep, rdate from `USER` where seq=?");
		pstmt.setInt(1, seq);
		//4. 쿼리 실행
		rs = pstmt.executeQuery();
		//5. 결과셋 처리(select 쿼리)
		while(rs.next()) { 
			user.setSeq(rs.getInt(1));
			user.setUId(rs.getString(2));
			user.setName(rs.getString(3));
			user.setHp(rs.getString(4));
			user.setAddr(rs.getString(5));
			user.setPos(rs.getString(6));
			user.setDep(rs.getInt(7));
			user.setRDate(rs.getString(8));
		}
	} catch(Exception e) {
		e.printStackTrace();
	} finally {
		//6. 데이터베이스 접속 종료
		if(rs != null)
			rs.close();
		if(pstmt != null)
			pstmt.close();
		if(conn != null)
			conn.close();
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>직원수정</title>
<script>
	function init(addr, pos, dep) {
		for(var i = 0; i<5; i++) {
			if(document.regForm.addr.options[i].value == addr) {
				document.regForm.addr.options[i].selected = true;
			} else {
				document.regForm.addr.options[i].selected = false;
			}
		}
		for(var i = 0; i<5; i++) {
			if(document.regForm.pos.options[i].value == pos) {
				document.regForm.pos.options[i].selected = true;
			}
		}
		for(var i = 0; i<5; i++) {
			if(document.regForm.dep.options[i].value == dep) {
				document.regForm.dep.options[i].selected = true;
			}
		}
	}
</script>
</head>
<body onload="init('<%=user.getAddr()%>','<%=user.getPos()%>','<%=user.getDep()%>')">
	<h3>직원등록</h3>
	<form action="14-6.jsp" method="post" name="regForm">
		<table border="1">
			<tr>
				<td>아이디</td>
				<td>
					<input type="text" name="seq" hidden="true" value="<%=seq%>">
					<input type="text" name="uid" value="<%=user.getUId() %>" readonly>
				</td>
			</tr>
			<tr>
				<td>이름</td>
				<td><input type="text" name="name" value="<%=user.getName()%>"></td>
			</tr>
			<tr>
				<td>전화번호</td>
				<td><input type="text" name="hp" maxlength="13" value="<%=user.getHp()%>"></td>
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
				<td colspan="2" align="right"><input type="submit" value="수정하기"></td>
			</tr>
		</table>
	</form>
</body>
</html>