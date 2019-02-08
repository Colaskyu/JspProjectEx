<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="cPath" value="${ pageContext.request.contextPath }"/>
<c:set var="cnt" value="${tc - pageVo.startNum + 1}" />
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>글목록</title> 
		<link rel="stylesheet" href="${ cPath }/css/style.css" />
	</head>
	<body>
		<div id="board">
			<h3>글목록</h3>
			<!-- 리스트 -->
			<div class="list">
				<p class="logout">${member.nick }님! 반갑습니다. <a href="${ cPath }/member/logout.do">[로그아웃]</a><p>
				<table>
					<tr>
						<td>번호</td>
						<td>제목</td>
						<td>글쓴이</td>
						<td>날짜</td>
						<td>조회</td>
					</tr>
					<c:forEach var="result" items="${blist }">
					<tr>
						<td>${cnt = cnt -1 }</td>
						<td><a href="${cPath}/view.do?seq=${result.seq}&pageNum=${pageVo.pageNum}">${result.title }</a>&nbsp;[${result.comment }]</td>
						<td>${result.nick }</td>
						<td>${result.rdate }</td>
						<td>${result.hit }</td>
					</tr>
					</c:forEach>
				</table>
			</div>
			<!-- 페이징 -->
			<nav class="paging">
				<span> 
				<c:if test="${pageVo.startPage != 1 }">
					<a href="${cPath}/list.do?pageNum=${pageVo.startPage-1}" class="prev">이전</a>
				</c:if>
				<c:forEach var="i" begin="${pageVo.startPage }" end="${pageVo.endPage }" step="1">
					<c:choose>
						<c:when test="${pageVo.pageNum == i}">
							<a href="${cPath}/list.do?pageNum=${i}" class="current">${ i }</a>
						</c:when>
						<c:otherwise>
							<a href="${cPath}/list.do?pageNum=${i}" class="num">${ i }</a>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${pageVo.endPage != pageVo.totalPage }">
					<a href="${cPath}/list.do?pageNum=${pageVo.endPage+1}" class="next">다음</a>
				</c:if>
				</span>
			</nav>
			<a href="${cPath }/write.do?pageNum=${pageVo.pageNum}" class="btnWrite">글쓰기</a>
		</div>
	</body>

</html>










