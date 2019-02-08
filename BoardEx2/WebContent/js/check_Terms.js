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