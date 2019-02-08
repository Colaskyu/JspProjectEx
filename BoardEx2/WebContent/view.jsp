<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="cPath" value="${ pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>글보기</title> 
		<link rel="stylesheet" href="./css/style.css" />
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<script>
			var seq = "";
			$(function(){
				//글 번호(key) 시작시 생성
				seq = $("#seq").val();
				//시작 시 댓글 출력
				comment(seq);
				
				//댓글 삭제클릭 시 삭제
				$(document).on("click", ".comments .del", function() {
			         var cmtSeq = $(this).parent().parent().find("input[name=seq]").val();
					$.ajax({
						url: "${cPath}/commentDelete.do",
						type: 'POST',
						data: {"seq":seq, "cmtSeq":cmtSeq},
						dataType: 'json',
						success: function(data){
							if(data.result == "success") {
								comment(seq);
							} else {
								alert(data.result);
							}
						}
					});
			    });
				
				//다운로드 클릭 - 다운로드 걸고 ajax로 다운로드 카운트 업데이트/ 재출력
				$(document).on("click", ".view .download", function() {
					var fname = $(".view #fname").val();
					var ofname = $(".view #ofname").val();
					//파일 다운로드는 get 방식으로 실행(return 없음)
					window.location.href = "${cPath}/download.do?seq="+seq+"&fname="+fname+"&ofname="+ofname;
					
					var json = {"seq":seq};
					//다운로드 이후 ajax를 통해서 다운로드 카운트 +1 / 해당 카운트 숫자를 화면에 재출력
					$.ajax({
						url: "${cPath}/download.do",
						type: 'POST',
						data: json,
						dataType: 'json',
						success: function(data){
							//2번 list만을 받아와서 jquery로 출력하는 방식
							var downloadCnt = $(".view .downloadCnt");
							downloadCnt.text(data.download + "회 다운로드");
						}
					});
				});
			});
			
			//댓글 표시
			function comment(seq) {
				var json = {"seq":seq};
				$.ajax({
					url: "${cPath}/commentView.do",
					type: 'POST',
					data: json,
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
									commentCloned.find('input[name=seq]').attr("value",data[i].seq);
									commentCloned.find('span > :nth-child(1)').text(data[i].nick);
									commentCloned.find('span > :nth-child(2)').text(data[i].rdate.substring(2,10));
									commentCloned.find('textarea').text(data[i].content);
									comment.find("div > :nth-child(1)").attr("href","javascript:void(0)");
									comments.append(commentCloned);
								} else {
									comment.find('input[name=seq]').attr("value",data[i].seq);
									comment.find('span > :nth-child(1)').text(data[i].nick);
									comment.find('span > :nth-child(2)').text(data[i].rdate.substring(2,10));
									comment.find('textarea').text(data[i].content);
									comment.find("div > :nth-child(1)").attr("href","javascript:void(0)");
								}
							}
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
					<input type="hidden" id="seq" value="${vo.seq }"/>
					<input type="hidden" id="fname" value="${vo.newFileName }"/>
					<input type="hidden" id="ofname" value="${vo.oldFileName }"/>
					<table>
						<tr>
							<td>제목</td>
							<td><input type="text" name="subject" value="${vo.title }" readonly />
							</td>
						</tr>
						<tr>
							<td>첨부파일</td>
							<td>
								<c:choose>
									<c:when test="${vo.file == 1 }">
										<a href="javascript:void(0)" class="download">${vo.oldFileName}</a>
										<span class="downloadCnt">${ vo.download }회 다운로드</span>
									</c:when>
									<c:otherwise>
										<span>첨부파일이 없습니다.</span>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td>내용</td>
							<td>
								<textarea name="content" rows="20" readonly>${vo.content }</textarea>
							</td>
						</tr>
					</table>
					<div class="btns">
					<c:if test="${vo.uid == member.uid }">
						<a href="${ cPath }/delete.do?seq=${vo.seq}" class="cancel del">삭제</a>
						<a href="${ cPath }/modify.do?seq=${vo.seq}&pageNum=${param.pageNum}" class="cancel mod">수정</a>
					</c:if>
						<a href="${ cPath }/list.do?pageNum=${param.pageNum}" class="cancel">목록</a>
					</div>
				</form>
			</div><!-- view 끝 -->
			
			<!-- 댓글리스트 -->
			<section class="comments">
				<h3>댓글목록</h3>
				
				<div class="comment">
					<input type="hidden" name="seq" value=""/>
					<span>
						<span>홍길동</span>
						<span>18-03-01</span>
					</span>
					<textarea readonly>테스트 댓글입니다.</textarea>
					<div>
						<a href="#" class="del">삭제</a>
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
					<form action="${cPath }/commentWrite.do" method="post">
						<input type="hidden" name="seq" value="${vo.seq }"/>
						<input type="hidden" name="pageNum" value="${param.pageNum }"/>
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