//-----------------------로그인 시 이메일 비밀번호 공란으로 두면 폼제출 막기------------------------------
const loginForm = document.querySelector('#login-form');
const memberEmail = document.querySelector('#member-email');
const memberPw2 = document.querySelector('#member-pw2');
const loginError = document.querySelector('#login-error');

if(loginForm != null){
	loginForm.addEventListener('submit', function(e){
		
		// 이메일 미작성
		if(memberEmail.value.trim().length === 0){
			loginError.innerText = '이메일을 작성해주세요';
			e.preventDefault();
			memberEmail.focus();
			return;
		}
		
		// 비밀번호 미작성
		if(memberPw2.value.trim().length === 0){
			loginError.innerText = '비밀번호를 작성해주세요';			
			e.preventDefault();
			memberPw2.focus();
			return;
		}
	
	});		
};