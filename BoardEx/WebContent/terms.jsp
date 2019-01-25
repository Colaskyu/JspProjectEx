<%@page import="kr.co.boardEx.vo.TermsVO"%>
<%@page import="kr.co.boardEx.service.MemberService"%>
<%@page import="java.sql.*"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	MemberService service = MemberService.getInstance();
	TermsVO vo = service.terms();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>terms</title>
<link rel="stylesheet" href="./css/style.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$(".btnNext").click(function() {
			if(!$("[name=chk1]").is(":checked")) {
				alert("사이트 이용약관을 동의해주시기 바랍니다.");
				return false;
			} else	if(!$("[name=chk2]").is(":checked")) {
				alert("개인정보 취급방침을 동의해주시기 바랍니다.");
				return false;
			} else {
				return true;
			}
		});
	});
</script>
</head>
<body>
	<div id="terms">
		<section>
			<table>
				<caption>사이트 이용약관</caption>
				<tr>
					<td>
						<textarea readonly><%=vo.getTerms() %></textarea>
						<div>
							<label><input type="checkbox" name="chk1" />&nbsp;동의합니다.</label>
						</div>
					</td>
				</tr>
			</table>
		</section>			
		<section>
			<table>
				<caption>개인정보 취급방침</caption>
				<tr>
					<td>
						<textarea readonly><%=vo.getPrivacy() %></textarea>
						<div>
							<label><input type="checkbox" name="chk2" />&nbsp;동의합니다.</label>        
						</div>
					</td>
				</tr>
			</table>
		</section>
		
		<div>
			<a href="./index.jsp" class="btnCancel">취소</a>
			<a href="./register.jsp" class="btnNext">다음</a>
		</div>
	</div>
</body>
</html>