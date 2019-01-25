<%@page import="kr.co.boardEx.service.MemberService"%>
<%@page import="kr.co.boardEx.vo.MemberVO"%>
<%@page import="kr.co.boardEx.SQL"%>
<%@page import="kr.co.boardEx.DBConfig"%>
<%@page import="java.sql.*"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	MemberService service = MemberService.getInstance();
	Boolean result = service.register(request);

	if(result) {
		response.sendRedirect("../login.jsp?register=success");
	} else {
		response.sendRedirect("../register.jsp?result=fail");
	}
%>