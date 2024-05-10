const pwInput = document.querySelector("#pwInput");
const pwConfInput = document.querySelector("#pwConfInput");

const withdrawalBtn = document.querySelector("#withdrawal2");
const mainBtn = document.querySelector("#goMain");


withdrawalBtn.addEventListener('click', function(e){
	let obj ={
	"pwInput" : pwInput.value,
	"pwConfInput" : pwConfInput.value
	}
	
	
	fetch( "/member/withdrawal",{
		method : "POST",
		headers : {"Content-Type":"application/json"},
		body : JSON.stringify(obj)
	})
	.then( resp=> resp.text())
	.then(result => {
		
		if(result > 0 ){
			alert("탈퇴가 완료되었습니다.");
			location.href = "/";
			
		}else {
			alert("비밀번호가 일치하지 않습니다.");
		} 	
		
	})
	
});



mainBtn.addEventListener("click" , function(){
	
	location.href = "/";
});

const changePw = document.querySelector("#changePw");
const checkPw = document.querySelector("#checkPw");

