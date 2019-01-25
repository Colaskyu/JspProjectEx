<%@page import="kr.co.boardEx.vo.MemberVO"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	MemberVO member = (MemberVO)session.getAttribute("member");
	if(member == null) {
		//response.sendRedirect("./login.jsp");
		pageContext.forward("./login.jsp");
	} else {
		//response.sendRedirect("list.jsp");
		pageContext.forward("./list.jsp");
	}
%>