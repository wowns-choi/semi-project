const changePw = document.querySelector("#changePw");
const changePwBtn= document.querySelector("#changePwBtn");

changePwBtn.addEventListener("click", function() {
	
	let changePwValue = changePw.value;
	
	fetch( "/member/changePassword",{
		method : "POST",
		headers :{"Content-Type":"application/json"}, 
		body : JSON.stringify({"changePw" : changePwValue})
	})
	.then( resp => resp.text() )
	.then( result => {
		
		//alert(result);
		
		if(result > 0 ){
			
			location.href = "/member/newPw";
		}else{
			alert("비밀번호를 확인해주세요");
		}
		
	})
});