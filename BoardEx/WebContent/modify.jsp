<%@page import="kr.co.boardEx.vo.BoardVO"%>
<%@page import="kr.co.boardEx.service.BoardService"%>
<%@page import="kr.co.boardEx.vo.MemberVO"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	//login session 정보
	MemberVO member = (MemberVO)session.getAttribute("member");
	String seq = request.getParameter("seq");
	String pageNum = request.getParameter("pageNum");
	BoardService service = BoardService.getInstance();
	int chkId = service.checkBoardId(session, Integer.parseInt(seq));
	if(chkId == 1) {
		response.sendRedirect("./view.jsp?seq="+seq+"&pageNum="+pageNum+"&errtype=1");
	}
	BoardVO board = service.view(request);
	String result = request.getParameter("result");
	if(member == null) {
		pageContext.forward("./login.jsp");
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link rel="stylesheet" href="./css/style.css" />
<script>
	var result = "<%=result%>";
	if(result.equals("fail")) {
		alert("서버상의 문제로 글 저장에 실패하였습니다. 관리자에게 문의해주세요.");
	}
</script>
</head>
<body>
	<div id="board">
		<h3>글쓰기</h3>
		<div class="write">
			<form action="./proc/modify.jsp" method="post" enctype="multipart/form-data">
				<input type="hidden" name="seq" value="<%=board.getSeq()%>"/>
				<input type="hidden" name="pageNum" value="<%=pageNum%>"/>
				<table>
					<tr>
						<td>제목</td>
						<td><input type="text" name="subject" placeholder="제목을 입력하세요." value="<%=board.getTitle() %>" required /></td>
					</tr>
					<tr>
						<td>내용</td>
						<td>
							<textarea name="content" rows="20" required><%=board.getContent() %></textarea>
						</td>
					</tr>
					<tr>
						<td>첨부</td>
						<td>
							<span><%=board.getOldFileName() %></span><br>
							<input type="file" name="file" />
						</td>
					</tr>
				</table>
				<div class="btns">
					<a href="javascript:history.go(-1)" class="cancel">취소</a>
					<input type="submit" class="submit" value="수정완료" />
				</div>
			</form>
		</div>
	</div>
</body>
</html>