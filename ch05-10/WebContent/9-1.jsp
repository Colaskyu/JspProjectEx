<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>9-1</title>
</head>
<body>
쿠키목록 <br>
<%
	Cookie cookie = new Cookie("name","cho");
	Cookie cookie1 = new Cookie("name1","한글");
	response.addCookie(cookie);
	response.addCookie(cookie1);
%>
</body>
</html>