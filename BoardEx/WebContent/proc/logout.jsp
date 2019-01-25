<%@page import="kr.co.boardEx.service.MemberService"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	MemberService.getInstance().logout(response, session);
%>