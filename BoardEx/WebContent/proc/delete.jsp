<%@page import="kr.co.boardEx.service.BoardService"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	String seq = request.getParameter("seq");
	String pageNum = request.getParameter("pageNum");
	BoardService service = BoardService.getInstance();
	String url = null;
	int chkId = service.checkBoardId(session, Integer.parseInt(seq));
	if(chkId == 1) {
		url = "../view.jsp?seq="+seq+"&errtype=1";
	} else {
		int result = service.deleteBoard(request);
		if(result == 2) {
			url = "../view.jsp?seq="+seq+"&errtype=2"; 
		} else {
			url = "../list.jsp?pageNum="+pageNum;
		}
	}
	response.sendRedirect(url);
%>