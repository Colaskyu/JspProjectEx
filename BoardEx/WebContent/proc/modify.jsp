<%@page import="kr.co.boardEx.service.BoardService"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	BoardService service = BoardService.getInstance();
	String url = service.modifyBoard(request, session);
	
	response.sendRedirect(url);
%>