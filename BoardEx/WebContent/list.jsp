<%@page import="kr.co.boardEx.service.BoardService"%>
<%@page import="kr.co.boardEx.vo.BoardPageVO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.co.boardEx.vo.BoardVO"%>
<%@page import="kr.co.boardEx.SQL"%>
<%@page import="java.sql.*"%>
<%@page import="kr.co.boardEx.DBConfig"%>
<%@page import="kr.co.boardEx.vo.MemberVO"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	//login session 정보
	MemberVO member = (MemberVO)session.getAttribute("member");
	//write 결과 param
	String result = request.getParameter("result");
	
	if(member == null) {
		pageContext.forward("./login.jsp");
	}

	BoardService service = BoardService.getInstance();
	
	BoardPageVO pageVo = service.listPage(request);
	
	List<BoardVO> blist = new ArrayList<>();

	blist = service.list(request, pageVo.getStartNum(), pageVo.getPageSize());
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록</title>
<link rel="stylesheet" href="./css/style.css" />
<script>
	var result = "<%=result%>";
	if(result.equals("success")) {
		alert("글 작성이 완료되었습니다.");
	}
</script>
</head>
<body>
	<div id="board">
		<h3>글목록</h3>
		<!-- 리스트 -->
		<div class="list">
			<p class="logout"><%=member.getNick() %>님! 반갑습니다. <a href="./proc/logout.jsp">[로그아웃]</a><p>
			<table>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>글쓴이</th>
					<th>날짜</th>
					<th>조회</th>
				</tr>
<%	for(BoardVO list : blist) { %>
				<tr>
					<td><%=list.getSeq() %></td>
					<td><a href="./view.jsp?seq=<%=list.getSeq()%>&pageNum=<%=pageVo.getPageNum()%>"><%=list.getTitle() %> </a>&nbsp;[<%=list.getComment() %>]
<%		if(list.getFile() == 1){ %>
					<img src="./img/ico_file.png" alt="file" height="12" width="10"/>
<%		} %>
					</td>
					<td><%=list.getNick() %></td>
					<td><%=list.getRdate().substring(2, 10)%></td>
					<td><%=list.getHit() %></td>
				</tr>	
<% 	} %>
			</table>
		</div>
		<!-- 페이징 -->
		<nav class="paging">
			<span> 
<%
	//시작 페이지가 1일 경우에는 이전 표시 X
	if(pageVo.getStartPage() != 1){ %>
			<a href="./list.jsp?pageNum=<%=pageVo.getStartPage() - 1 %>" class="prev">이전</a>
<% }

	for(int i = pageVo.getStartPage(); i <= pageVo.getEndPage(); i++) { 
		if(i == pageVo.getPageNum()) { %>
			<a href="./list.jsp?pageNum=<%=i %>" class="current"><%=i %></a>
<%	} else { %>
			<a href="./list.jsp?pageNum=<%=i %>" class="num"><%=i %></a>	
<%	}
	}
	//마지막 페이지가 총 페이지일 경우에는 다음 표시 X
	if(pageVo.getEndPage() != pageVo.getTotalPage()){%>
			<a href="./list.jsp?pageNum=<%=pageVo.getEndPage()+1 %>" class="next">다음</a>
<% } %>
			</span>
		</nav>
		<a href="./write.jsp" class="btnWrite">글쓰기</a>
	</div>
</body>
</html>