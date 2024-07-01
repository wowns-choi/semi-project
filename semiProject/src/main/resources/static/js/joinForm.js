// 규칙 : 카테고리는 * 로 구분. 각 기능에서 선택자로 쓴 건 = 로 표시해둠.

const checkObj = {
    "memberEmail" : false,
    "authKey" : false,
    "memberPw" : false,
    "memberPwConfirm" : false,
    "memberNickname" : false,
    "memberTel" : false
};

// ============================선택자로 선택한 요소들=============================
const inputEmail = document.querySelector('#input-email'); // 이메일 입력 input태
const emailError = document.querySelector('#email-error'); // 이메일이 정규식 위배할 경우, message를 띄울 div 태
// *******************************이메일********************************** 

inputEmail.addEventListener('input', function(e){
	// 1) 이메일 인증이 된 이후, 사용자가 이 input 태그에 값을 변경한 경우, 
	
	// 2) inputEmail에 사용자가 입력한 값 가져오기.
	const inputEmailValue = inputEmail.value;
	//console.log(inputEmailValue);
	
	// 3) 사용자가 이미 이메일 입력 input 태그에 값을 썻었다가 싹 지웠을 때, 문구를 보여주기 위함.
	if(inputEmailValue.trim().length === 0){
		emailError.innerText = '* 이메일을 입력해주세요. 비밀번호 분실시 입력하신 이메일로 메일이 발송됩니다.'
		
		// 메세지에 색상을 추가하는 클래스 모두 제거
		emailError.classList.remove('confirm', 'error');
		
		// 이메일 유효성 검사 여부를 false 로 변경
	    checkObj.memberEmail = false;
	    
	    // 이메일 input 창에 아무것도 입력하지 않았을 경우
	    // 띄어쓰기가 쓰여지지 못하도록 함. 
	    
	    inputEmail.value = "";
	    
	    return; 
	}
	// 4) 입력된 이메일이 있을 경우
	// 4-1) 정규식 검사 시작 
	const regExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
	if(!regExp.test(inputEmailValue)){
		// 정규식에 걸린 경우(실패한 경우)
		emailError.innerText='알맞은 이메일 형식으로 작성해주세요.';
		emailError.classList.add('error'); // 문구를 빨강색으로
		emailError.classList.remove('confirm'); // 문구 초록색으로 하는 css 제거
		checkObj.memberEmail = false; // 정규식 통과 + 중복 검사 후에 다시 input 을 입력한 경우도 있을 수 있으므로.
		return;		
	}
	
	// 4-2) 정규식에 통과한 경우 이므로, 중복 검사 시작. 
	fetch('/member/checkEmail?memberEmail=' + inputEmailValue)
	.then(resp => resp.text())
	.then(count => {
		if(count > 0){
			// 이메일이 중복되는 회원이 이미 있는 경우
			emailError.innerText = '이미 사용중인 이메일 입니다.';
			emailError.classList.add('error');
			emailError.classList.remove('confirm');
			checkObj.memberEmail = false; // 중복은 유효하지 않으므로
			return;
		} else{
			// 이메일이 중복되는 회원이 없는 경우
			emailError.innerText = '사용 가능한 이메일 입니다.';
			emailError.classList.add('confirm');
			emailError.classList.remove('error');
			checkObj.memberEmail = true; // 중복은 유효하지 않으므로
		}
	})
	.catch(error => {
		console.log('이메일 중복 fetch 실행 중 오류 발생', error); // 발생한 예외 출력
	});
});

// ============================선택자로 선택한 요소들=============================
const sendAuthKeyBtn = document.querySelector('#send-auth-key-btn'); // 인증번호 받기 버튼
const InputAuthKey = document.querySelector('#auth-key'); // 인증번호 입력 input 태그
const authKeyError = document.querySelector('#auth-key-error') // 인증번호가 일치하지 않을 때 문구를 표시할 div 태그.

// ******************인증번호 받기 버튼을 클릭할 시 벌어질 일들에 대하여*****************
let authTimer; // 인증번호 받기 버튼을 누르면, 타이머(setInterval)가 시작될텐데, 그 타이머를 담을 변수.

const initMin = 4; // 타이머 초기값 (분)
const initSec = 59; // 타이머 초기값 (초)
const initTime = "05:00";

// 실제 줄어드는 시간을 저장할 변수
let min = initMin; // min = 4 겠지.
let sec = initSec; // sec = 59 겠지. 

// 인증번호 받기 버튼을 누르는 순간, 이메일 발송해줘야함. 
sendAuthKeyBtn.addEventListener('click', function(e){
	
	// 인증번호 받기 버튼을 클릭했다면, 지금 회원가입하려고 하는 사용자가 
	// 이미 인증을 받았던 못받았던 간에, "인증을 받지 않은 사용자" 로 간주하겠다.
	checkObj.authKey = false;
	authKeyError.innerText = ''; // 이메일 인증 시도했다가 실패한 경우, authKeyError 라는 div 태그에는 인증에 실패했다는 메세지가 들어있을 것인데, 다시 인증번호 받기 버튼을 클릭했을 경우, 이를 지워주겠다는 의미로 씀.
	

	if(!checkObj.memberEmail){ 	// 정규식 검사에 통과하지 못했거나, 중복된 아이디가 있을 경
		alert('유효한 이메일 작성 후 클릭해주세요');
		return;		
	} 
	
	// 여기까지 왔다는 건 사용자가 입력한 이메일이 정규식 검사에도 통과했고, 중복되지도 않았다는 뜻.
	// 여기서 앞으로 해야 되는 건, 크게 2가지로 분류된다. 
	// 1. fetch 로 서버에 이메일 보내라고 하는 것. 
	// 2. 타이머를 시작하는 것이다. 
	
	// 1. fetch 로 서버에 이메일 보내라고 하기. 
	fetch("/email/sendEmail", {
		method : "POST",
		headers : {"Content-Type" : "application/json"},
		body : inputEmail.value	// 단순 문자열 형태의 하나의 데이터만 보내는 경우.
	})
	.then(resp => resp.text())
	.then(result => {
		if(result > 0){
			alert('이메일이 성공적으로 발송되었습니다.');
		} else{
			alert('이메일 전송 중 오류 발생');
		}
	})
	.catch(error => {
		console.log('이메일 보내는 중 예외발생', error); 
	});
	
	// 2. 타이머 시작하기
	// 일단, 인증번호 받기 버튼을 클릭했을 경우, 
	min = initMin; // min = 4
	sec = initSec; // sec = 59 로 세팅하고,
	
	// 인증번호 받기 버튼을 이전에 클릭했을 경우, 실행되고 있을 타이머를 지운다. 
	clearInterval(authTimer);
	
	// 인증번호 받기 버튼을 누르자마자 보여질 05:00 을 세팅하는 모습. 검정글씨로 나오기 위해, css 도 제거.
    authKeyError.innerText = initTime; // 05:00 세팅
    authKeyError.classList.remove("confirm", "error");
	
	// 
	authTimer = setInterval(function() {
    authKeyError.innerText = addZero(min) + ":" + addZero(sec);
    if(min == 0 && sec == 0){
        checkObj.authKey = false;
        clearInterval(authTimer);
        authKeyError.classList.add('error');
        authKeyError.classList.remove('confirm');
        return;
    }
    if(sec == 0){
        sec = 60;
        min--;
    }
    sec--;
	}, 1000); // setInterval 함수를 호출하면, 반환되는 건 타이머를 식별할 수 있는 타이머 ID 이다. 
			// 이 타이머 ID 를 authTimer 라는 변수에 담아둔 것이다. 
			// 그리고 이 authTimer 라는 변수를 통해 clearInterval(타이머ID) 라고 하면 타이머가 중지되는 등으로 사용할 수 있다. 
	
})

// 전달 받은 숫자(number)가 10 미만인 경우 앞에 0 붙여서 반환해주는 함수
function addZero(number){
    if( number < 10 ) return "0" + number;
    else return number;
}

// ============================선택자로 선택한 요소들=============================
const checkAuthKeyBtn = document.querySelector('#check-auth-key-btn'); // 인증하기 버튼.
// 위에서 선택해 놓음. const InputAuthKey = document.querySelector('#auth-key'); // 인증번호 입력 input 태그
// 위에서 선택해 놓음. const authKeyError = document.querySelector('#auth-key-error') // 인증번호가 일치하지 않을 때 문구를 표시할 div 태그.

// ****************************인증하기 버튼을 눌렀을 경우 ************************
checkAuthKeyBtn.addEventListener('click', function(e){
	// 인증하기 버튼을 누르면~
	
	// 1. 시간초가 지났는지 판단
	if(min == 0 && sec == 0){
		alert('시간초과');
		return;
	}
	
	// 시간초과되지 않았을 경우
	// 2. 6자리가 맞는지 확인. 6자리보다 작다면, db 갈 필요 없이 틀렸다고 알려줘야 함.  
	// 왜 굳이 db 가서 일치하는지 보면 될텐데, 여기서 해줬냐면, 최대한 db까지 안가도록 하기 위함. 
	
	if(InputAuthKey.value.length < 6){
		alert('인증번호가 일치하지 않습니다.');
		return;
	}
	
	// 6자리라면, db 에서 인증번호 꺼내와서 대조해봐야 함. 
	const obj = {
		"email" : inputEmail.value,
		"authKey" : InputAuthKey.value
	};
	
	fetch("/email/checkAuthKey", {
		method : "POST",
		headers : {'Content-Type' : "application/json"},
		body : JSON.stringify(obj),		
	})
	.then(resp => resp.text())
	.then(result => {
		
		console.log('aaaaaaaaaaaa' + result);
		
		if(result == 0){
			alert('인증번호가 일치하지 않습니다!');
			checkObj.authKey = false; // 왜? 인증했다가 다시 인증번호발급받아서 다시 시도한 경우를 고려
			return;
		}
		
		// 여기까지 왔다는 건? 인증번호가 일치한다는 뜻
		clearInterval(authTimer);  // 타이머 중지시킴.
		authKeyError.innerText = '인증 되었습니다'; // 문구 띄워주고,
		authKeyError.classList.remove('error');
		authKeyError.classList.add('confirm');
		
		checkObj.authKey = true; // 유효성 검사 성공했다 라고 표시
		return;
		
	})
	.catch(error => {
		console.log('인증번호 일치 검사 중 예외 발생', error);
	});
	
	
})

// ============================선택자로 선택한 요소들=============================
const memberPw = document.querySelector('#member-pw'); // 비밀번호 input 태그
const memberPwConfirm = document.querySelector('#member-pw-confirm');// 비밀번호 확인 input 태그
const passwordError = document.querySelector('#password-error'); // 비밀번호 에러 메세지 div 태그

// ****************************비밀번호 + 비밀번호 확인 ************************

//   '비밀번호' 유효성 검사 시작
// 1) 비밀번호 input 창에 input 을 했다면~
memberPw.addEventListener('input', function(e){
    const inputPw = e.target.value;
	
	// 2) 아무것도 입력하지 않은 경우 <- 비밀번호 썻다가 모두 지웠을 경우를 대비함.
    if(inputPw.trim().length == 0){
        passwordError.innerText = "영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요.";
        passwordError.classList.remove("confirm", "error");
        checkObj.memberPw = false; // 비밀번호가 유효하지 않다고 표시
        memberPw.value = ""; // 처음에 띄어쓰기 입력 못하게 하기 
        return;
    }

    // 3) 입력한 것이 있는 경우, 정규표현식으로 유효성 검사 시작.
    const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;
    if(!regExp.test(inputPw)){
        // 3-1) 유효성 검사 실패
        passwordError.innerText="비밀번호가 유효하지 않습니다";
        passwordError.classList.add("error");
        passwordError.classList.remove("confirm");
        checkObj.memberPw = false;
        return;
    }
    
    // 3-2) 유효성 검사 성공(정규표현식 통과) 
    passwordError.innerText = '유효한 비밀번호 형식입니다';
    passwordError.classList.add("confirm");
    passwordError.classList.remove("error");
    checkObj.memberPw = true;
    
    // 여기까지 왔다는 건, 비밀번호에 대한 유효성 검사가 성공했다는 뜻.

    // 6) "비밀번호 = 비밀번호 확인" 이라면, checkObj.memberPwConfirm 을 true 로 해줘야함. 
    // 다시 말하면, 비밀번호 확인 은 정규표현식 검사를 할 필요가 없고, 비밀번호 와 같은지만 검사해주면 됨. 

    // 비밀번호 확인창에 사용자가 뭘 쓰면(input이벤트) 이 이벤트리스너 동작. 
    memberPwConfirm.addEventListener("input", () => {
		// 비밀번호가 유효성 검사에 통과했고, 비밀번호 확인란에 뭐가 쓰여졌을 경우에
        if(checkObj.memberPw && memberPwConfirm.value.trim().length > 0){
			// checkPw() 함수를 호출함으로써, 비밀번호가 비밀번호확인과 같은지 여부를 검사해서
			// checkObj.memberPwConfirm 의 값을 변환시키는 함수를 호출하겠다. 
            checkPw();
            return;
        }
		// 비밀번호가 유효성 검사에 통과하지 못했거나, 비밀번호 확인란에 아무것도 쓰여지지 않았다면
		// checkObj.memberPwConfirm 의 값을 false 로 두어서 나중에 폼이 제출되지 못하도록 하겠다. 
        checkObj.memberPwConfirm = false;
    })
    
	// 이 코드는 왜 썻을까? 
	// 사용자가 비밀번호 확인란에 먼저 값을 입력하고, 비밀번호를 쓴 경우에는 어떻게 될까? 
	// 바로 위에있는 이벤트리스너는 비밀번호 확인란에 뭘 입력했을 때에만 동작하기 때문에, 
	// 비밀번호 확인 먼저 쓰고, 비밀번호를 썼는데, 비밀번호가 유효성 검사에 통과한 경우, 
	// 폼 제출이 막히도록은 해놓을거고, alert() 로 그 이유에 대해 설명은 해주겠지만, 
	// 사용자 경험이 저하됨. 
	// 따라서, 비밀번호 확인란에 값이 들어갔다면 무조건 checkPw() 를 호출해줌으로써, 
	// 비밀번호 == 비밀번호 확인이 일치하는지 여부에 따라 메세지를 띄워줄 수 있도록 함. 
    if(memberPwConfirm.value.length > 0){
        checkPw();
    }
    
})

//  비밀번호, 비밀번호확인이 같은지 검사하는 함수
const checkPw = () => {
    // 같을 경우
    if(memberPw.value === memberPwConfirm.value){
        passwordError.innerText = "비밀번호가 일치합니다";
        passwordError.classList.add("confirm");
        passwordError.classList.remove("error");
        checkObj.memberPwConfirm = true;
        return; 
    }

    // 같지 않을 경우
    passwordError.innerText = "비밀번호가 일치하지 않습니다";
    passwordError.classList.add("error");
    passwordError.classList.remove("confirm");
    checkObj.memberPwConfirm = false;
}

// ============================선택자로 선택한 요소들=============================
const memberNickname = document.querySelector('#member-nickname'); // 비밀번호 input 태그
const nameError = document.querySelector("#name-error");
// ****************************이름 유효성 검사 ********************************

memberNickname.addEventListener("input", (e) => {
	// 이름 input 창에 쓰여진 것 을 inputNickname 이라는 변수에 담아둠.
    const inputNickname = e.target.value;

    // 1) 아무것도 입력 안한 경우
    if(inputNickname.trim().length ===0){
        nameError.innerText = "* 한글, 영어, 숫자로만 2~10글자";
        nameError.classList.remove("confirm", "error");
        checkObj.memberNickname = false;
        memberNickname.value = "";
        return;
    }

    // 2) 정규 표현식 
    const regExp = /^[가-힣\w\d]{2,10}$/;
    if(!regExp.test(inputNickname)){
        // 정규표현식에 걸려서 실패한 경
        nameError.innerText = "유효하지 않은 닉네임 형식입니다.";
        nameError.classList.add("error");
        nameError.classList.remove("confirm");
        checkObj.memberNickname = false;
        return;
    }
    
    // 3) 여기까지 왔다는 건, 정규표현식에 통과했다는 것.
    // 선생님은 이름도 중복되지 않도록 fetch 를 써서 db 에 동일한 nickname 이 없는 경우에만
    // checkObj.memberNickname 의 값을 true 로 해주었지만, 
    // 나는 이름 중복을 허용할 것이다.
	// 따라서, 여기까지 온 경우, checkObj.memberNickname = true 로 해줄것이다. 
    nameError.innerText = "유효한 닉네임 입니다.";
    nameError.classList.add("confirm");
    nameError.classList.remove("error");
	checkObj.memberNickname = true;    
})
// ============================선택자로 선택한 요소들=============================
const memberTel = document.querySelector('#member-tel'); // 핸드폰 번호 input 태그
const phoneError = document.querySelector("#phone-error"); // 핸드폰 번호 관련 메세지 출력 div태그
// ****************************핸드폰 번호 유효성 검사 ********************************

memberTel.addEventListener('input', function(e){
	// 1) 아무것도 입력하지 않은 경우
    if(memberTel.value.trim().length == 0){
        phoneError.innerText = '전화번호를 입력해주세요.(- 제외)';
        phoneError.classList.remove("confirm");
        phoneError.classList.remove("error");

        checkObj.memberTel = false; //이거 왜 썻냐면, 유효한 전화번호였다가 지웠을 수 있잖아. 

        memberTel.value = ""; // 아무것도 안썻을 경우 띄어쓰기 불허
    }
	
	// 2) 뭐라도 입력한 경우
	// 2-1) 정규표현식 
    const regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;

    if(!regExp.test(e.target.value)){
        //정규식을 통과하지 못한 경우
        phoneError.innerText = '전화번호 형식이 올바르지 않습니다. ';
        phoneError.classList.add("error");
        phoneError.classList.remove("confirm");
        checkObj.memberTel = false;
        return;
    }
	
	// 정규표현식에 통과한 경우
    phoneError.innerText = '유효한 전화번호 형식입니다. ';
    phoneError.classList.add("confirm");
    phoneError.classList.remove("error");
    checkObj.memberTel = true;
    return;	
})



// ============================선택자로 선택한 요소들=============================
const signupForm = document.querySelector("#sing-up-form");
// *************** Sign Up 버튼 클릭 시,  checkObj 가 모두 true 인 경우에만 폼제출이 되도록 할 거임.*******************

const postcode = document.querySelector("#postcode");

signupForm.addEventListener('submit', function(e){

	const detailAddress = document.querySelector("#detailAddress");

	if(postcode.value != ''){
		if(detailAddress.value == ''){
			alert('상세 주소를 입력해 주세요');
			e.preventDefault();
			return;
		}
	}

    // for ~ in (객체 전용 향상된 for 문)
    for (let key in checkObj){ // checkObj 요소의 key 값을 순서대로 꺼내옴
        if(!checkObj[key]){ // false 인 경우 
            let str; // 출력할 메세지를 저장할 변수

            switch(key){
                case "memberEmail" :
                    str = "이메일이 유효하지 않습니다."; break;
                case "authKey" :
                    str = "이메일이 인증되지 않았습니다."; break;
                case "memberPw" :
                    str = "비밀번호가 유효하지 않습니다."; break;
                case "memberPwConfirm" :
                    str = "비밀번호가 일치하지 않습니다."; break;
                case "memberNickname" :
                    str = "닉네임이 유효하지 않습니다."; break;
                case "memberTel" :
                    str = "전화번호가 유효하지 않습니다."; break;
            }
            alert(str);
            
            //여기서는 이제, 유효성 검사에 실패한 친구들을 focus 해줘야 함. 
            let focus;
            switch(key){
				case "memberEmail" :
					focus = "#input-email"; break;
				case "authKey" :
					focus = "#auth-key"; break;
				case "memberPw" :
					focus = "#member-pw"; break;
				case "memberPwConfirm" :
					focus = "#member-pw-confirm"; break;
				case "memberNickname" :
					focus = "#member-nickname"; break;
				case "memberTel" :
					focus = "#member-tel"; break;
			}

			let beFocused = document.querySelector(focus);
			beFocused.focus();
            e.preventDefault(); // form 태그 기본 이벤트(제출) 막기
            return;

        }
        
    }


})

