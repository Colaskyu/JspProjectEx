<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="cPath" value="${ pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link rel="stylesheet" href="./css/style.css" />
</head>
<body>
	<div id="board">
		<h3>글쓰기</h3>
		<div class="write">
			<form action="${cPath }/modify.do" method="post" enctype="multipart/form-data">
				<input type="hidden" name="seq" value="${vo.getSeq() }"/>
				<input type="hidden" name="pageNum" value="${param.pageNum }"/>
				<table>
					<tr>
						<td>제목</td>
						<td><input type="text" name="subject" placeholder="제목을 입력하세요." value="${vo.getTitle() }" required /></td>
					</tr>
					<tr>
						<td>내용</td>
						<td>
							<textarea name="content" rows="20" required>${vo.getContent() }</textarea>
						</td>
					</tr>
					<tr>
						<td>첨부</td>
						<td>
							<span>${vo.getOldFileName() }</span><br>
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