const memberNickname = document.querySelector("#member-nickname");
const memberTel = document.querySelector("#member-tel");
const foundId = document.querySelector("#found-id");

/*
foundId.addEventListener('click', function() {
	
	let memberNicknameValue = memberNickname.value;
	let memberTelValue = memberTel.value;
	
	let obj = {
		
		"memberNickname" : memberNicknameValue,
		"memberTel" : memberTelValue
	}
	
	fetch("/member/foundId",{
		
 		method : "POST",
		headers : {"Content-Type" : "application/json"},
		body : JSON.stringify(obj)
	})
	.then(resp => resp.text() )
	.then(result =>{
		
		alert(result);
		if (result > 0) {
			
			location.href = "/member/idResult"; // Controller 가 받는 경로 == location.href
		} else {
			alert("일치하는 사용자의 정보가 없습니다. 아이디 또는 휴대폰 번호를 확인해주세요");
		}
		
	} )
});
*/

memberTel.addEventListener('input', function(event) {
    // 입력된 값이 숫자가 아니면 제거
    this.value = this.value.replace(/[^0-9]/g, '');
});
