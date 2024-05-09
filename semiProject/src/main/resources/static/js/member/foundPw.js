const memberName = document.querySelector("#member-name");
const memberEmail = document.querySelector("#member-mail");

const getAuthKeyBtn = document.querySelector("#get-auth-key-Btn");


getAuthKeyBtn.addEventListener("click", function() {
	
	let obj = {
		
		"memberNickname" : memberName.value,
		"memberEmail" : memberEmail.value
	}
	
	fetch("/member/getAuth",{
		method : "POST",
		headers: {"Content-Type" : "application/json"},
		body : JSON.stringify(obj)
		
	})
	.then(resp => resp.text())
	.then( result =>{
		
		if(result > 0 ){
			
			alert("인증번호가 발송되었습니다");
			
		}else {
			alert("인증번호를 확인해주세요");
		}
	})
	
});

const authKey = document.querySelector("#auth-key");
const changePwBtn = document.querySelector("#change-pw-btn");

changePwBtn.addEventListener("click", function() {
	
	 let obj = {
		"memberEmail" : memberEmail.value,
		"authKey" : authKey.value
	 }
     

	
	fetch("/member/authKey",{
		method: "POST",
		headers : {"Content-Type" : "application/json"},
		body : JSON.stringify(obj)	
	})
	.then(resp => resp.text() )
	.then(data => {})
	
	
});
	
	
	
	
	
	
	