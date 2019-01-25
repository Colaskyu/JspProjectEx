<%@page import="kr.co.boardEx.service.MemberService"%>
<%@page import="kr.co.boardEx.vo.MemberVO"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	
	MemberService service = MemberService.getInstance();
	boolean result = service.login(request, session);
	
	if(result) {
		response.sendRedirect("../list.jsp");	
	} else {
		response.sendRedirect("../login.jsp?register=fail");
	}
%>