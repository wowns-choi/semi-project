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

const getCookie = (key) => {

    const cookies = document.cookie;

    const cookieList = cookies.split("; ").map( el => el.split("="));

    const obj = {}; // 비어있는 객체 선언

    for(let i = 0 ; i < cookieList.length ; i++) {
        const k = cookieList[i][0]; // key 값
        const v = cookieList[i][1]; // value 값
        obj[k] = v; // 객체에 추가
    }

    return obj[key];

}

if(memberEmail != null) {

	const saveId = getCookie("saveId"); // undefined 또는 이메일
    // 아이디 저장 체크 안 했을 때 undefined
    // 체크 했을 때는 이메일 넘어옴

    // saveId 값이 있을 경우
    if(saveId != undefined) {
        memberEmail.value = saveId; // 쿠키에서 얻어온 값을 input에 value로 세팅

        // 아이디 저장 체크박스에 체크 해두기
        document.querySelector("input[name='saveId']").checked = true;
    }
}