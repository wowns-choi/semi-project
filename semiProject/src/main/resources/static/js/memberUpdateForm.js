const editBtn = document.querySelector('#edit-btn'); //edit 버튼
const pointContainer = document.querySelector('#point-container'); //오른쪽 컨테이너 안에 있는 모든 요소를 감싼 컨테이너
const rightContainer = document.querySelector('#right-container'); //오른쪽 컨테이너

const saveBtn = document.querySelector('#save-btn'); //저장하기 버튼

const findAddr = document.querySelector('.find-address-btn');

//감출 요소들 선택자로 모두 선택. 감춰질 요소라고 해서 hidden 의 h 를 따서 뒤에 붙였다.  
const userName_h = document.querySelector('#basic-info-div-1>div:nth-child(2)'); // 로그인한 사용자의 이름

let phone_h;
let postCode_h;
let roadAddress_h;
let jibunAddress_h;
let detailAddress_h;

if(document.querySelector('#phone-div') != null){
	phone_h = document.querySelector('#phone-div'); // 로그인한 사용자의 핸드폰
}
if(document.querySelector('#post-code-div-2') != null){
	postCode_h = document.querySelector('#post-code-div-2'); // 로그인한 사용자의 우편번호	
}
if(document.querySelector('#road-address-div-2') != null){
	roadAddress_h = document.querySelector('#road-address-div-2'); // 로그인한 사용자의 도로명 주소
}
if(document.querySelector('#jibun-address-div-2') != null){
	jibunAddress_h = document.querySelector('#jibun-address-div-2'); // 로그인한 사용자의 지번 주소	
}
if(document.querySelector('#detail-address-div-2') != null){
	detailAddress_h = document.querySelector('#detail-address-div-2'); // 로그인한 사용자의 상세 주소
}

//보여줄 요소들 선택자로 모두 선택. 보여줄 요소라고 해서 show 의 s 를 따서 뒤에 붙였다. 
const userName_s = document.querySelector('#basic-info-div-1>input:nth-child(3)'); // 로그인한 사용자의 이름
const email_s = document.querySelector('#email-domain-div-2');  // 로그인한 사용자의 이메일
const onlyEmail_s = document.querySelector('#email-domain-div-2>input:nth-child(1)'); // 이메일만
const onlyDomain_s = document.querySelector('#email-domain-div-2>input:nth-child(3)'); // 도메인만
const phone_s = document.querySelector('#update-pw-input'); // 로그인한 사용자의 핸드폰
const postCode_s = document.querySelector('#sample4_postcode'); // 로그인한 사용자의 우편번호
const roadAddress_s = document.querySelector('#sample4_roadAddress'); // 로그인한 사용자의 도로명 주소
const jibunAddress_s = document.querySelector('#sample4_jibunAddress'); // 로그인한 사용자의 지번 주소
const detailAddress_s = document.querySelector('#sample4_detailAddress'); // 로그인한 사용자의 상세 주소
const phoneText = document.querySelector('#phone-text');

// 요소에 있는 데이터 꺼내오기. 
let userNameData = userName_h.innerText;
let phoneData;
let postCodeData;
let roadAddressData;
let jibunAddressData;
let detailAddressData;
if(document.querySelector('#phone-div') != null){
	phoneData = phone_h.innerText;
}
if(document.querySelector('#post-code-div-2') != null){
	postCodeData = postCode_h.innerText;	
}
if(document.querySelector('#road-address-div-2') != null){
	roadAddressData = roadAddress_h.innerText;	
}
if(document.querySelector('#jibun-address-div-2') != null){
	jibunAddressData = jibunAddress_h.innerText;
}
if(document.querySelector('#detail-address-div-2') != null){
	detailAddressData = detailAddress_h.innerText;
}




let flag = false;
editBtn.addEventListener('click', function (e) {
    if (flag == false) {
        flag = true;

        // 일단, 오른쪽 컨테이너의 배경색은 어둡게 한다 
        // 그림자 효과는 줘놨기 때문에, 여기서 안 건듦.
        rightContainer.style.backgroundColor = '#707070';

        // 수정하기(Edit) 버튼 -> 취소하기(Cancel) 버튼으로 변경
        editBtn.innerHTML = '';
        let btnInnerHTML = document.createElement('span');
        btnInnerHTML.innerHTML = 'CANCEL';
        editBtn.append(btnInnerHTML);

        // 저장하기(save) 버튼 보여주기
        saveBtn.style.display = 'block'

        // 감출 요소 감추기 
        userName_h.style.display = 'none';
        if(document.querySelector('#phone-div') != null){
	        phone_h.style.display = 'none';
		}
		if(document.querySelector('#post-code-div-2') != null){
        	postCode_h.style.display = 'none';		
		}
		if(document.querySelector('#road-address-div-2') != null){
	        roadAddress_h.style.display = 'none';
		}
		if(document.querySelector('#jibun-address-div-2') != null){
	        jibunAddress_h.style.display = 'none';
		}
		if(document.querySelector('#detail-address-div-2') != null){
        	detailAddress_h.style.display = 'none';
		}

        // 보여줄 요소 보여주기
        userName_s.style.display = 'block';
        email_s.style.display = 'flex';
        phone_s.style.display = 'block';
        
        postCode_s.style.display = 'block';
        roadAddress_s.style.display = 'block';
        jibunAddress_s.style.display = 'block';
        detailAddress_s.style.display = 'block';
        findAddr.style.display = 'block'; // 우편번호 찾기 버튼도 보여줌.
        phoneText.style.display = 'block';

        // 보여진 요소에 내용 담을 것이다. 
        userName_s.value = userNameData;


        if(document.querySelector('#phone-div') != null){
	        phone_s.value = phoneData;
		}
		if(document.querySelector('#post-code-div-2') != null){
        	postCode_s.value = postCodeData;			
		}
		if(document.querySelector('#road-address-div-2') != null){
	        roadAddress_s.value = roadAddressData;
		}
		if(document.querySelector('#jibun-address-div-2') != null){
	        jibunAddress_s.value = jibunAddressData;
		}
		if(document.querySelector('#detail-address-div-2') != null ){
	        detailAddress_s.value = detailAddressData;			
		}
		



    } else {
        // Cancel 버튼을 누른 경우가 되겠지. 
        //다시 원래대로 복귀.

        flag = false;

        // 일단, 오른쪽 컨테이너의 배경색은 다시 밝게 해준다. 
        // 그림자 효과는 줘놨기 때문에, 여기서 안 건듦.
        rightContainer.style.backgroundColor = '#fafafa';

        // 취소하기(Cancel) 버튼 -> 수정하기(Edit) 버튼으로 변경
        editBtn.innerHTML = '';
        let btnInnerHTML = document.createElement('span');
        const userPen = document.querySelector('.fa-user-pen');
        btnInnerHTML.append(userPen);
        btnInnerHTML.innerHTML = 'EDIT';
        
        editBtn.append(btnInnerHTML);

        // 저장하기(save) 버튼 감추기
        saveBtn.style.display = 'none';

        // 보여줬던 요소 다시 감추기
        userName_s.style.display = 'none';
        email_s.style.display = 'none';
        phone_s.style.display = 'none';
        postCode_s.style.display = 'none';
        roadAddress_s.style.display = 'none';
        jibunAddress_s.style.display = 'none';
        detailAddress_s.style.display = 'none';
        findAddr.style.display = 'none'; // 우편번호 찾기 버튼도 보여줌.
        phoneText.style.display = 'none';


        // 감췄던 요소 다시보여주기 
        userName_h.style.display = 'block';
        if(document.querySelector('#basic-info-div-3 > div:nth-child(2)') != null){
        	phone_h.style.display = 'block';
		}
		if(document.querySelector('#post-code-div-2') != null){
        	postCode_h.style.display = 'block';			
		}
		if(document.querySelector('#road-address-div-2') != null){
	        roadAddress_h.style.display = 'block';
		}
		if(document.querySelector('#jibun-address-div-2') != null){
	        jibunAddress_h.style.display = 'block';
		}
		if(document.querySelector('#detail-address-div-2') != null){
	        detailAddress_h.style.display = 'block';
		}
    }
})

//--------------------------------유효성 검사-------------------------------------
document.querySelector('#update-form').addEventListener('submit', function(e){
	// 이름은 카카오톡으로 로그인해서 회원가입이 된 경우, 카카오톡에서 이름이라고 보내준 값이 들어오게 되는데, 
	// 이 값이 2~5글자의 한글로만 이루어져 있지 않다. 
	// 우연찮게 내 아이디가 wowns590 이렇게 되서 알았다. 
	// 그래서 공백인지 아닌지만 검사할 것. 
	if(userName_s.value == ''){
		alert('이름을 입력해주세요');
		userName_s.focus();
		e.preventDefault();
		return;
	}
	
	// 이메일은 아이디로 사용되는 부분이라서, 변경이 불가능하도록 함. 
	// 카카오톡이나 네이버로 로그인한 경우에는, 아이디 비밀번호 분실시 우리 웹사이트에서 아이디/비밀번호를 찾는게 아니라
	// 카카오톡이나 네이버 서버에 요청을 해야 되는 부분이기에 신경 안씀. 
	
	// 배송정보도 선택사항이기 때문에, 별다른 유효성검사는 안해줌. 
	
	// 결론적으로, 핸드폰번호에 대한 유효성검사만 해줄거임. 
	const memberTel = document.querySelector('#update-pw-input');
	// 1) 아무것도 입력하지 않은 경우
    if(memberTel.value.trim().length == 0){
		alert('전화번호를 입력해주세요.');
        memberTel.value = ""; // 아무것도 안썻을 경우 띄어쓰기 불허
        memberTel.focus();
        e.preventDefault();
       	return;
    }
    
    // 2) - 입력하지 말랬는데, 입력한 경우
    if(memberTel.value.includes('-')){
		alert('전화번호에 - 를 제외하고 입력해주세요.');
		memberTel.focus();
        e.preventDefault();
		return;
	}
    
	
	// 3) 정규표현식 
    const regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;

    if(!regExp.test(memberTel.value)){
        //정규식을 통과하지 못한 경우
        alert('전화번호 형식이 올바르지 않습니다.');
		memberTel.focus();    
        e.preventDefault();    
        return;
    }
	
	// 주소 안쓰려면 다 안쓰도록 해야함.
	if(postCode_s.value != ''){
		if(detailAddress_s.value == ''){
			alert('상세 주소를 입력해 주세요');
			e.preventDefault();
			return;
		}
	}
	
	if(detailAddress_s.value != ''){
		if(postCode_s.value == ''){
			alert('우편번호 찾기를 통해 주소를 찾아주세요');
			e.preventDefault();
			return;
		}
	}
});


//---------------------------------------------------------------------




/* 프로필 이미지 추가/변경/삭제 */
// 프로필 이미지 페이지 form 태그
const profile = document.querySelector("#profile");
// 프로필 이미지가 새로 업로드 되거나 삭제 되었음을 기록하는
// 상태 변수
// -1 : 초기 상태(변화 없음)
//  0 : 프로필 이미지 삭제
//  1 : 새 이미지 선택
let statusCheck = -1;
// input type="file" 태그의 값이 변경 되었을 때
// 변경된 상태를 백업해서 저장할 변수
// -> 파일이 선택/취소된 input을 복제해서 저장
// 요소. cloneNode(true|false) : 요소 복제(true 작성 시 하위 요소도 복제)
let backupInput;


 

 


// profile form태그가 화면에 있다면
if(profile != null){
		// 1) 프로필 이미지 수정에 사용할 요소 모두 얻어오기
		 // img 태그 (프로필 이미지가 보여지는 요소)
		 const profileImg = document.querySelector("#profileImg");
		 // input type="file" 태그 (실제 업로드할 프로필 이미지를 선택하는 요소)
		 let imageInput = document.querySelector("#imageInput");
		 // x버튼 (프로필 이미지를 제거하고 기본 이미지로 변경하는 요소)
		 const deleteImage = document.querySelector("#deleteImage");
		
		 
		 // 3) changeImageFn 함수 정의하기
		 /* input type="file"의 값이 변했을 때 동작할 함수(이벤트 핸들러) */
		 const changeImageFn = e => {
					   // 업로드 가능한 파일 최대 크기 지정하여 필터링
					   const maxSize =  1024 * 1024 * 5;
					   // 5MB == 1024KB * 5 == 1024B * 1024 * 5
					   console.log("e.target", e.target); // input 태그
					   console.log("e.target.value", e.target.value); // 변경된 값(파일명)
					   // 선택된 파일에 대한 정보가 담긴 배열 반환
					   // -> 왜 배열?? multiple 옵션에 대한 대비(파일 여러개 받을 때)
					   console.log("e.target.files", e.target.files);
					   // 업로드된 파일이 1개 있으면 files[0]에 저장됨
					   // 업로드된 파일이 없으면 files[0] == undefined
					   console.log("e.target.files[0]", e.target.files[0]);
					   const file = e.target.files[0];
					   
				   // ------------ 업로드된 파일이 없다면(취소한 경우)------------
				   // 이 경우, value = "" 이렇게 빈문자열이 되고, 
				   //		 files 라는 배열형태의 속성은 비어 있게됨. 
				   // 자바스크립트에서 존재하지 않는 배열의 인덱스에 접근하려고 할 경우, undefined 를 반환함. 
				  if(file == undefined){
				     console.log("파일 선택 후 취소됨");
				    
				     // 파일 선택 후 취소 -> value == ''
				     // -> 선택한 파일 없음으로 기록됨
				     // -> backupInput으로 교체 시켜서
				     //    이전 이미지가 남아 있는 것 처럼 보이게 함
				     // 백업의 백업본
				     
				     // 현재 그런 코드는 보이지 않지만, backupInput 이라는 변수에
				     // 파일선택을 누르기 전 <input type="file"> 이 들어있었다고 가정하고 이어가보자. 
				     const temp = backupInput.cloneNode(true); // true 로 두면 깊은복사(자식까지 복사함)
				     										   // false 로 두면 얕은복사(자식은 복사되지 않음)
				    
				     console.log("temp", temp); // 백업용 input태그
				     // input 요소 다음에 백업 요소 추가
				     imageInput.after(backupInput);
				    
				     // 화면에 존재하는 기존 input 제거. html 상에서 삭제됨. 그러나 변수인 imageInput 은 여전히 존재함.
				     imageInput.remove();
				     // imageInput 변수에 백업을 대입해서 대신하도록 함. 
				     imageInput = backupInput;
				     // 화면에 추가된  백업본에는
				     // 이벤트 리스너가 존재하지 않기 때문에 추가
				     imageInput.addEventListener("change", changeImageFn);
				     // 한번 화면에 추가된 요소(backupInput)는 재사용 불가능
				     //  backupInput의 백업본이 temp를 backupInput 으로 변경
				     backupInput = temp;
				     return;  // 다른 코드 수행할필요없이 바로 return
				   }
				  
				  
				   // ----------- 선택된 파일이 최대 크기를 초과한 경우 ------------
				   if(file.size > maxSize){
				     alert("5MB 이하의 이미지 파일을 선택해 주세요.");
						//파일을 선택할 때 5MB보다 큰 파일을 선택하면
						//일단 무조건 선택은 됨.
						//근데 우리는 5MB보다 큰 파일은 취급 안하고 싶음
						//그래서 대입된 5MB 초과한 파일을 없애버리겠다
						
				     // 아직 변경된적없는 초기상태에서 5MB 초과하는 이미지를 선택한 경우
				     if(statusCheck == -1){
						// 아래 코든느 <input type="file"> 의 value 속성값을 '' 로 하겠다는 건데,
						// <input type="file"> 의 value 속성값을 '' 로 하겠다는 건,
						// <img> 태그를 없애겠다는 게 아니라는 걸 조심해야해.  
				       imageInput.value = '';
				   
				     } else{ // 기존에 변경하려고 선택한 이미지가 있는데
				             // 다음에 선택한 이미지가 최대 크기를 초과한 경우
				             // -> 비워버리면 안되고, 그 전에 선택한 이미지로 계속 보이게끔 처리해야함.
				       // 백업의 백업본
				       const temp = backupInput.cloneNode(true);
				       // input 요소 다음에 백업 요소 추가
				       imageInput.after(backupInput);
				       // 화면에 존재하는 기존 input 제거
				       imageInput.remove();
				       // imageInput 변수에 백업을 대입해서 대신하도록 함
				       imageInput = backupInput;
				       // 화면에 추가된  백업본에는
				       // 이벤트 리스너가 존재하지 않기 때문에 추가
				       imageInput.addEventListener("change", changeImageFn);
				       // 한번 화면에 추가된 요소(backupInput)는 재사용 불가능
				       //  backupInput의 백업본이 temp를 backupInput 으로 변경
				       backupInput = temp;
				     }
				     return; // 다른 코드 수행할필요없이 바로 return
				   }
				 
				   // ------------- 선택된 이미지 미리보기 ----------------
				   // JS에서 파일을 읽을 때 사용하는 객체
				   // - 파일을 읽고 클라이언트 컴퓨터에 저장할 수 있음
				   /*FileReader 객체는 웹 애플리케이션에서 비동기적으로 파일의 내용을 읽을 수 있게 해줍니다. */
				   const reader = new FileReader();
				   // 선택한 파일(file) 을 읽어와
				   // BASE64 인코딩 형태로 읽어와 result 변수에 저장
				   reader.readAsDataURL(file); // -> 읽어오기 이벤트(load)
				   // readAsDataURL() : 파일을 BASE64 형식의 데이터 URL로 읽어들입니다.
				   
				   
				   // console.log("reader:",reader);
				   // result에 "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAW" 이런식으로 들어감
				   // 읽어오기 끝났을 때 (파일 읽기 작업이 완료되면 이벤트 핸들러 함수를 실행)
				   reader.addEventListener("load", e => {
				     // e.target == reader
				     // 읽어온 이미지 파일이 BASE64 형태로 반환됨
				     const url = e.target.result; // reader.result
				     // 프로필 이미지(img)에 src속성으로 url값 세팅
				     profileImg.setAttribute("src", url);
				    
				     // 새 이미지 선택 상태를 기록
				     statusCheck = 1;
				     // 파일이 선택된 input을 복제해서 백업
			         // !!! 이 코드가 backupInput 을 최초로 초기화 하는 부분이야.
				     backupInput = imageInput.cloneNode(true); 
				  
				   });
 		 };
 		
 		
 		
		 // 2) imageInput에 change 이벤트로 changeImageFn 등록
 		 // change 이벤트 : 새로운 값이 기존 값과 다를 경우 발생
 		 // type="file" 인 input 태그에게 change 이벤트는 어떤 이벤트를 의미할까?
 		 // 파일선택을 눌러서 파일을 선택할 수 있는 창이 나오면
 		 // 파일을 선택해서 열기(업로드) 버튼을 누르는 순간, type="file" 인 input 태그의
 		 // 2가지 속성이 변경된다. 
 		 // 하나는 value 속성이다. 이 속성은, 보안상의 이유로 보여주지 않는다. 
 		 // 나머지 하나는 files 속성이다. 파일 여러개(multiple)를 업로드하는 것을 대비해서,
 		 // 배열로 만들어졌는데, 이 배열 안에 업로드 된 파일의 이름, 크기, 타입 등이 저장되어있다. 
 		 // change 이벤트는 이 2가지 속성 중 value 속성의 변화를 감지해서 일어난다.
 		 // !!!!!!!!!!!!!!!!! 이게 포인트 !!!!!!!!!!!!!!!
 		 // 파일선택을 누르고, 취소버튼을 눌렀을 때, change 이벤트리스너가 반응하나? 
 		 // 이전에 파일선택 -> 열기(업로드) 를 누른 적이 있었을 때에만! 반응한다. 
 		 // 따라서, 최초에 파일선택을 누르고, 취소버튼을 눌렀다면, 이벤트리스너는 동작하지 않아서
 		 // changeImageFn 이라는 함수는 실행되지 않는다. 
 		 // changeImageFn 이라는 함수가 실행되지 않으면? 아무일도 안일어난다는 거야. 
 		 // 아무리 반복해서 취소버튼을 누르더라도 아무런 동작도 일어나지 않는다는 거야. 
 		 
 		 // 그리고, 코드에 나타나 있지 않은 정보가 하나 있는데,  
 		 // 만약 파일선택 -> 열기(업로드) 를 했다면? 
 		 // 자동으로 <input type="file"> 의 value 속성값과 files 속성값(배열)이 채워진다는 거임. 
 		 // 그런데, <img> 태그의 src 는 바꿔준적이 없잖아. 그래서, changeImageFn 라는 함수에서  그 일을 해주고 있음. 
 		 // 그게 // ------------- 선택된 이미지 미리보기 ---------------- 밑에 있는거임. 
 		  		  
 		 // 그 다음 선택지는 3가지가 있음. 
 		 // 1. x 버튼을 눌렀을 경우 -> 4) x버튼 클릭 시 기본 이미지로 변경 으로 가서 
 		 // 						 <img> 태그의 src 속성값을 user.png 로 바꿔서 기본이미지로 변경하고,
 		 //							 <input type="file"> 의 value 속성값을 비워둠 왜? 
 		 imageInput.addEventListener("change", changeImageFn); 
		
	
	
	
		 // ------------ 4) x버튼 클릭 시 기본 이미지로 변경 ----------------
		 deleteImage.addEventListener("click", () => {
		   // 프로필 이미지(img)를 기본 이미지로 변경
		   profileImg.src = "/images/user.png";
		   // input에 저장된 값(value)를 ''(빈칸)으로 변경
		   //   -> input에 저장된 파일 정보가 모두 사라짐 == 데이터 삭제
		   
		   // value 를 '' 로
		   imageInput.value = ''; // 어차피, 폼 버튼 누르면, 전달되는 건 files 인데?
		   
		    
		    
		   backupInput = undefined; // 백업본도 삭제. 왜 굳이? 
		   // 삭제 상태임을 기록
		   statusCheck = 0;
		 });
		 
		 
		 
		 
		 // ------------ 5) #profile (form) 제출 시 -----------------
		 profile.addEventListener("submit", e => {
		  
		   let flag = true;
			// loginMemberProfileImg : myPage-profile.html 하단에 script를 이용하여 타임리프로 선언해둔 변수
			
			// submit 해도 되는 경우 :
		   // 1. 기존 프로필 이미지가 없다가 새 이미지가 선택된 경우
		   // 기존 이미지가 없어서 기본 이미지가 보여져 있었는데, 새로운 이미지를 선택한 경우
		   if(loginMemberProfileImg == null && statusCheck == 1) flag = false;
		   
		   // 2. 기존 프로필 이미지가 있다가 삭제한 경우
		   // 기존 이미지가 있었는데, x버튼 누른 경우
		   if(loginMemberProfileImg != null && statusCheck == 0) flag = false;
		  
		   // 3. 기존 프로필 이미지가 있다가 새 이미지가 선택된 경우
		   // 기존 이미지가 있었는데, 새 이미지를 선택한 경우
		   if(loginMemberProfileImg != null && statusCheck == 1) flag = false;
			// 나머지의 경우는 기존 상태에서 변경사항이 없는 경우임.-> 제출막기
		   if(flag){ // flag 값이 true인 경우
		     e.preventDefault();
		     alert("이미지 변경 후 클릭하세요")
		   }
		 });
		 
		 
		 
		 
}
/*  [input type="file" 사용 시 유의 사항]
 1. 파일 선택 후 취소를 누르면
   선택한 파일이 사라진다  (value == '')
 2. value로 대입할 수 있는 값은  '' (빈칸)만 가능하다
 3. 선택된 파일 정보를 저장하는 속성은
   value가 아니라 files이다
*/



