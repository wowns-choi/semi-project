const changePw = document.querySelector("#changePw");



if(changePw != null){
	
	
	changePw.addEventListener("submit", e=> {
		
		const currentPw = document.querySelector("#currentPw");
		const currentPwConf = document.querySelector("#currentPwConf");
		
		let str;
		
		if(currentPw.value.trim().length == 0) str ="새로 사용할 비밀번호를 입력해주세요";
		else if(currentPwConf.value.trim().length == 0) str ="새로 사용할 비밀번호를 재입력 해주세요";
		
		if(str != undefined){
			alert(str);
			e.preventDefault();
			return;
			
		}
		
		const regExp = /^[a-zA-Z0-9!@#$_-]{6,20}$/;
		
		if( !regExp.test(currentPw.value)){
			alert("입력하신 비밀번호가 유효하지 않습니다.");
			e.preventDefault();
			return;
		}
		
		if(currentPw.value != currentPwConf.value){
			alert("변경할 비밀번호와, 비밀번호 확인이 일치하지 않습니다.");
			e.preventDefault();
			return;
			
		}
	});
}