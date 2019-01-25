<%@page import="com.google.gson.Gson"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.co.boardEx.vo.BoardVO"%>
<%@page import="kr.co.boardEx.service.BoardService"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	BoardService service = BoardService.getInstance();
	ArrayList<BoardVO> blist = new ArrayList<>();
	blist = service.viewComment(request);
	//1. html코드를 직접작성하여 본 페이지로 전송하는 방식
	//JSON 데이터 출력용 object (library 파일로부터 인식)
//	JSONObject json = new JSONObject();
	
//	String rHtml = "<h3>댓글목록</h3>";
//	if(blist.size() != 0) {
//		for(BoardVO vo : blist) {
//			rHtml = rHtml + "<div class='comment'>";
//			rHtml = rHtml + "<span>";
//			rHtml = rHtml + "<span>"+vo.getNick()+"</span> ";
//			rHtml = rHtml + "<span>"+vo.getRdate().substring(2,10)+"</span>";
//			rHtml = rHtml + "</span>";
//			rHtml = rHtml + "<textarea readonly>"+vo.getContent() + "</textarea>";
//			rHtml = rHtml + "<div>";
//			rHtml = rHtml + "<a href='javascript:void(0)' onclick='commentDelete("+vo.getSeq()+")' class='del'>삭제</a> ";
//			rHtml = rHtml + "<a href='./proc/CommentUpdate.jsp?seq="+vo.getSeq()+"' class='mod'>수정</a>";
//			rHtml = rHtml + "</div>";
//			rHtml = rHtml + "</div>";
//		}
//		json.put("html",rHtml);
//	} else {
//		rHtml = rHtml + "<div class='comment'>";
//		rHtml = rHtml + "<p id='empty'>등록된 댓글이 없습니다.</p>";
//		rHtml = rHtml + "</div>";
//		json.put("html",rHtml);
//	}
//	out.println(json);
	
	//2. Gson 사용 방법 (list 를 json 형태로 변환 가능) -> list만을 view 페이지로 전송하는 방식 
	Gson gson = new Gson();
	String jsonl = gson.toJson(blist);
	out.println(jsonl);
%>