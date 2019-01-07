<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>9-2</title>
</head>
<body>
	<h3>2. 쿠키값 확인</h3>
	<%
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			out.println("<h4>");
			out.println(cookie.getName() + "<hr>");
			out.println(cookie.getValue() + "<hr>");
			out.println("</h4>");
		}
	%>
</body>
</html>