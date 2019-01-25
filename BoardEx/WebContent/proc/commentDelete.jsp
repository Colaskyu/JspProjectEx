<%@page import="org.json.simple.JSONObject"%>
<%@page import="kr.co.boardEx.service.BoardService"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	BoardService service = BoardService.getInstance();
	int seq =Integer.parseInt(request.getParameter("seq"));
	int cmtSeq =Integer.parseInt(request.getParameter("cmtSeq"));
	int chkId = service.checkBoardId(session, cmtSeq);
	
	//JSON 데이터 출력용 object (library 파일로부터 인식)
	JSONObject json = new JSONObject();
	if(chkId == 1) {
		json.put("error","1");
		out.println(json);
	} else {
		json.put("error","0");
		out.println(json);
		service.commentDelete(cmtSeq);
		service.commentCnt(seq);
	}
	
%>