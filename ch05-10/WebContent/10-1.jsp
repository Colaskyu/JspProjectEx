<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>10-1</title>
</head>
<body>
	<h3>1. 세션</h3>
	<%
		session.setAttribute("name","홍길동");
		session.setMaxInactiveInterval(60 * 1);
		out.println((String)session.getAttribute("name"));
	%>
	<h4>서버에 값 저장 완료</h4>
	<a href="10-2.jsp">10-2</a>
</body>
</html>