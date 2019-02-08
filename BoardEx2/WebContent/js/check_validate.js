/**
아이디 닉네임 이메일 휴대폰 중복 체크
**/
var isUidOk = false;
var isNickOk = false;
var isEmailOk = false;
var path = '${cPath}';

$(function(){
		$('input[name=id]').keyup(function(){
			var tag = $(this)
			var uid = tag.val();
			$.ajax({
				url: path+"/member/validate.do",
				type: 'post',
				data:{'type':'name', 'param':$('input[name=id]').val()},
				dataType: 'json',
				success: function(data){
					$(".resultId").css("color",data.color).text(data.result);
					if(data.color != "red") {
						isUidOk = true;
					} else {
						isUidOk = false;
					}
				}
			});
		});
		
		$('input[name=nick]').keyup(function() {
			var tag = $(this)
			var nick = tag.val();
			$.ajax({
				url: "./proc/checkNick.jsp?nick=" + nick,
				type: 'get',
				dataType: 'json',
				success: function(data){
					$(".resultNick").css("color",data.color).text(data.result);
					if(data.color != "red") {
						isNickOk = true;
					} else {
						isNickOk = false;
					}
				}
			});
		});
		
		$('input[name=email]').keyup(function() {
			var tag = $(this)
			var email = tag.val();
			$.ajax({
				url: "./proc/checkEmail.jsp?email=" + email,
				type: 'get',
				dataType: 'json',
				success: function(data){
					$(".resultEmail").css("color",data.color).text(data.result);
					if(data.color != "red") {
						isEmailOk = true;
					} else {
						isEmailOk = false;
					}
				}
			});
		});
		
		$('#regForm').submit(function(){
			var uid = $("input[name=id]").val();
			var pw1 = $("input[name=pw1]").val();
			var pw2 = $("input[name=pw2]").val();
			var name = $("input[name=name]").val();

			//아이디 유효성 체크
			if(!isUidOk) {
				alert($(".resultId").text());
				$("input[name=id]").focus();
				return false;
			}
			
			//비밀번호 일치여부 확인
			if(pw1 != pw2) {
				alert("비밀번호가 일치하지 않습니다..");
				$("input[name=pw1]").focus();
				return false;
			}
			//이름 영문 숫자 포함여부 체크
			
			
			//닉 유효성 체크
			if(!isNickOk) {
				alert($(".resultNick").text());
				$("input[name=nick]").focus();
				return false;
			}
			
			//이메일 유효성 체크
			if(!isEmailOk) {
				alert($(".resultEmail").text());
				$("input[name=email]").focus();
				return false;
			}
		});
	});