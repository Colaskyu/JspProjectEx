<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>LOG OUT</title>
</head>
<body>
	<h3>로그아웃</h3>
	<%
		session.invalidate();
	%>
	<p>
		로그아웃 완료<br>
		<a href="login.jsp">로그인 화면으로 가기</a>
	</p>
	
</body>
</html>