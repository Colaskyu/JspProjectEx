<%@ page import="kr.co.boardEx.service.BoardService"%>
<%@ page import="kr.co.boardEx.vo.BoardVO"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	BoardService service = BoardService.getInstance();
	BoardVO board = service.view(request);
	//String errtype = request.getParameter("errtype");
	service.updateHit(board.getSeq());	
	String pageNum = request.getParameter("pageNum");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글보기</title>
<link rel="stylesheet" href="./css/style.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
	var seq = "";
	$(function(){
		seq = $("#seq").val();
		//시작 시 댓글 출력
		comment(seq);
		
/* 		$(".del").click(function(){
			alert($(".del").href);
		}); */
		
/*   		$(".submit").submit(function(){
			if($("textarea[name=comment]").val.equals("")) {
				alert("댓글 내용을 입력하세요.");
				return false;
			}
			
			$.ajax({
				url: "./proc/commentWrite.jsp",
				type: 'post',
				dataType: 'json',
				success: function(data){
					$(".comments").html(data.html);
				}
			});
			
			comment();
		});  */
	});
	
	//댓글 표시
	function comment(seq) {
		$.ajax({
			url: "./proc/commentView.jsp?seq="+seq,
			type: 'get',
			dataType: 'json',
			success: function(data){
				//1번 html을 그대로 받아서 출력하는 방식
				//$(".comments").html(data.html);
				
				//2번 list만을 받아와서 jquery로 출력하는 방식
				var comments = $(".comments");
				var comment = $(".comments > .comment");
				var empty = $(".comments > .empty"); 
				
				if(data.length == 0) {
					comment.remove();
				} else {
					empty.remove();
					//기존에 표시되어있던 comment 태그를 삭제(1개 남기고)
					for(var j = comment.length; j > 1; j--) {
						$(".comments > div:nth-of-type("+j+")").remove();
					}
					//1개 남은 태그를 다시 comment로 지정
					comment = $(".comments > .comment");
					//기존 comment에 내용 덧 씌우고 복사해서 출력
					for(var i in data) {
						if(i > 0) {
							var commentCloned = comment.clone();
							commentCloned.find('span > :nth-child(1)').text(data[i].nick);
							commentCloned.find('span > :nth-child(2)').text(data[i].rdate.substring(2,10));
							commentCloned.find('textarea').text(data[i].content);
							commentCloned.find("div > a:nth-of-type(1)").attr("href","javascript:void(0)");
							commentCloned.find("div > a:nth-of-type(1)").attr("onclick","commentDelete("+data[i].seq+")");
							commentCloned.find("div > a:nth-of-type(2)").attr("href","javascript:void(0)");
							commentCloned.find("div > a:nth-of-type(2)").attr("onclick","commentUpdate("+data[i].seq+")");
							comments.append(commentCloned);
						} else {
							comment.find('span > :nth-child(1)').text(data[i].nick);
							comment.find('span > :nth-child(2)').text(data[i].rdate.substring(2,10));
							comment.find('textarea').text(data[i].content);
							comment.find("div > :nth-child(1)").attr("href","javascript:void(0)");
							comment.find("div > :nth-child(1)").attr("onclick","commentDelete("+data[i].seq+")");
							comment.find("div > :nth-child(2)").attr("href","javascript:void(0)");
							comment.find("div > :nth-child(2)").attr("onclick","commentUpdate("+data[i].seq+")");
						}
					}
				}
			}
		});
	}
	
	function commentDelete(cmtSeq) {
		//삭제 json 처리
		$.ajax({
			url: "./proc/commentDelete.jsp?seq="+seq+"&cmtSeq="+cmtSeq,
			type: 'get',
			dataType: 'json',
			success: function(data){
				if(data.error =="1") {
					alert("작성자만 삭제가능합니다.");
				} else {
					alert("댓글삭제완료");
					//댓글 refresh
					comment(seq);
				}
			}
		});
	}
</script>
</head>
<body>
	<div id="board">
		<h3>글보기</h3>
		<div class="view">
			<form action="#" method="post">
				<input type="hidden" id="seq" value="<%=board.getSeq()%>"/>
				<table>
					<tr>
						<td>제목</td>
						<td><input type="text" name="subject" value="<%=board.getTitle() %>" readonly />
						</td>
					</tr>
					
					<tr>
						<td>첨부파일</td>
						<td>
<%	if(board.getFile() == 1) { %>
							<a href="./proc/download.jsp?seq=<%=board.getSeq() %>&fname=<%=board.getNewFileName()%>&ofname=<%=board.getOldFileName()%>"><%=board.getOldFileName() %></a>
							<span><%=board.getDownload() %>회 다운로드</span>
<%	} else { %>
							첨부파일이 없습니다.
<%	} %>
						</td>
					</tr>
					
					<tr>
						<td>내용</td>
						<td>
							<textarea name="content" rows="20" readonly><%=board.getContent() %></textarea>
						</td>
					</tr>
				</table>
				<div class="btns">
					<a href="./proc/delete.jsp?seq=<%=board.getSeq() %>&pageNum=<%=pageNum %>" class="cancel del">삭제</a>
					<a href="./modify.jsp?seq=<%=board.getSeq() %>&pageNum=<%=pageNum %>" class="cancel mod">수정</a>
					<a href="./list.jsp?pageNum=<%=pageNum %>" class="cancel">목록</a>
				</div>
			</form>
		</div><!-- view 끝 -->
		
		<!-- 댓글리스트 -->
		<section class="comments">
				<div class="comment">
					<span>
						<span></span>
						<span></span>
					</span>
					<textarea readonly></textarea>
					<div>
						<a href="#" class="del">삭제</a>
						<a href="#" class="mod">수정</a>
					</div>
				</div>
				<p class="empty">
					등록된 댓글이 없습니다.
				</p>
		</section>
		
		<!-- 댓글쓰기 -->
		<section class="comment_write">
			<h3>댓글쓰기</h3>
			<div>
				<form action="./proc/commentWrite.jsp" method="post">
					<input type="hidden" name="seq" value="<%=board.getSeq()%>"/>
					<textarea name="comment" rows="5"></textarea>
					<div class="btns">
						<a href="#" class="cancel">취소</a>
						<input type="submit" class="submit" value="작성완료" />
					</div>
				</form>
			</div>
		</section>
	</div><!-- board 끝 -->
</body>
</html>