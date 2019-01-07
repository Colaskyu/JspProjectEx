<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.User" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>LOGIN Proc</title>
</head>
<body>
	<h3>로그인 처리</h3>
	<%
		request.setCharacterEncoding("UTF-8");
		String uid  = request.getParameter("uid");
		String pass = request.getParameter("pass");
		
		//아이디 abcd 비밀번호 1234일 경우 ok
		if(!uid.equals("abcd") || !pass.equals("1234")) {
			response.sendRedirect("loginResult.jsp?result=fail");
		} else {
			User user = new User();
			user.setUid(uid);
			user.setPass(pass);
			user.setName("홍길동");
			session.setAttribute("user",user);
			
			response.sendRedirect("loginResult.jsp?result=success");
		}
	%>
</body>
</html>