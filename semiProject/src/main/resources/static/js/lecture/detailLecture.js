const selectDateSpan = document.querySelector('#select-date-span');
const restNumSpan = document.querySelector('#rest-num-span');
let selectDate;

document.addEventListener('DOMContentLoaded', function() {
  var calendarEl = document.getElementById('calendar');
  var calendar = new FullCalendar.Calendar(calendarEl, {
    locale: 'ko',
	initialView: 'dayGridMonth',
	initialDate: startDate,
    dateClick: function(info) {

		
		// 허용된 범위 내의 날짜를 선택했는지 검증하는 코드임. 
	        const selectedDate = new Date(info.dateStr); // 선택된 날짜가 2024-01-01 형식으로 info.dateStr 이라는 string 형식으로 들어오면, 이를 Date 타입으로 바꿈. 
	        const start = new Date(startDate); // db 에서 꺼내온 강의시작날짜를 Date 형식으로 바꿈.
	        const end = new Date(endDate); // db 에서 꺼내온 강의시작날짜를 Date 형식으로 바꿈.
			
			selectDate = info.dateStr;
	
	        // 선택된 날짜가 허용된 범위 내에 있는지 확인
	        if (selectedDate >= start && selectedDate <= end) {
	            // 날짜 선택 처리
	            var prevSelected = calendarEl.querySelector('.selected-date');
	            if (prevSelected) {
	                prevSelected.classList.remove('selected-date');
	            }
	            info.dayEl.classList.add('selected-date');
	            selectDateSpan.innerText = formatDate(info.dateStr);
	        } else {
	            // 선택 차단
	            alert('이 날짜는 선택할 수 없습니다.');
	            return;
	        }
        
		// 날짜 선택하면 색깔 바뀌는 거 구현한것.
		// 이전에 선택된 날짜의 클래스를 제거합니다.
			var prevSelected = calendarEl.querySelector('.selected-date');
			if (prevSelected) {
			  prevSelected.classList.remove('selected-date');
			}
			
			// 새로 선택된 날짜에 클래스를 추가합니다.
			info.dayEl.classList.add('selected-date');
		
		
		
		// 날짜 : 여기에 데이터 바인딩
		let str = formatDate(info.dateStr);		
		selectDateSpan.innerText = str;
		
		// info.dateStr => 선택한 날짜가 2024-05-05 형태의 문자열로 선택됨. 
		fetch('/lecture/checkRestnum?lectureNo=' + lectureNo + '&lectureDate=' + info.dateStr)
		.then(resp => resp.text())
		.then(result => {
			restNumSpan.innerText=result;

		})
		

		
		

    },
    events: [
      {
      start: startDate,
      end: date2,
      allDay: true,
      title: "기간",
      classNames: ['custom-class'] // 이벤트에 추가할 CSS 클래스
      
      }
    ],
    eventClassNames: function(arg) {
    return ['additional-class']; // 추가적으로 클래스를 지정
    }
  });
  calendar.render();
});


// 2024-04-03 -> 2024년 04월 03일  형식으로 바꾸는 함수.
function formatDate(inputDate) {
  // 문자열을 Date 객체로 변환.
  const date = new Date(inputDate);

  // 년, 월, 일을 각각 추출합니다.
  const year = date.getFullYear(); // 년도
  const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 월 (월은 0부터 시작하므로 1을 더해줍니다.)
  const day = date.getDate().toString().padStart(2, '0'); // 일

  // 원하는 문자열 포맷으로 조합합니다.
  return `${year}년 ${month}월 ${day}일`; // 참고로 자바스크립트내에서 백틱을 쓰면 자바스크립트 내에서 선언한 변수를 문자열 내에 포함시킬 수 있다. 
}

let totalPrice = parseInt(document.querySelector('#total-price').innerText); // 이때 totalPrice 를 자바스크립트 number 타입으로 바꿔줌 
console.log(totalPrice+'sssssss'+typeof totalPrice);  // string 인 거 확인. 

// 수량 바꿔줌 + 수량에 따라 총 결제금액도 바꿔줌
function changeQuantity(change) {
    const quantityInput = document.getElementById('quantity');
    let currentQuantity = parseInt(quantityInput.value);
    currentQuantity += change; // 일단,현재숫자가 뭐든 +를 눌렀든 -를 눌렀든 수량이 변하게 함.  
    if (currentQuantity < 1) { // 근데, 수량이 1보다 작으면, 그냥 1로 하도록 한다. 
        currentQuantity = 1; 
    }
    quantityInput.value = currentQuantity;
    
    
    // 총결제금액 바꾸기
    let pricePer = document.querySelector('#price-per').innerText;
    let pricePerInt = parseInt(pricePer);
    totalPrice = pricePerInt * currentQuantity;
    
    document.querySelector('#total-price').innerText = totalPrice;    
}

// 결론적으로 totalPrice 는 수량을 늘릴 때에나, 가만히 있을 때에나 
// 상관없이 모두 javascript number 타입이 되었음. 


//====================================================================================

// 리플 textarea에 입력하기 시작하면 '리뷰' 버튼 색깔 바꾸기
var textarea2 = document.getElementById('write-content');

let reviewWriteBtn = document.querySelector('#review-write-btn');

if(textarea2 != null){
	textarea2.addEventListener('input', function(){
		if(textarea2.value == ''){
			reviewWriteBtn.style.backgroundColor = 'lightgray';	
			reviewWriteBtn.style.color = 'gray';	
		} else{
			reviewWriteBtn.style.backgroundColor = '#006aff';	
			reviewWriteBtn.style.color = 'white';	
		}
	});
}

document.querySelectorAll('.dapgle-textarea').forEach(tex => {
    tex.addEventListener('input', function(e){
        if (this.value === '') {
            this.nextElementSibling.style.color = 'gray';
            this.nextElementSibling.style.backgroundColor = 'lightgray';
        } else {
			this.nextElementSibling.style.color = 'white';
            this.nextElementSibling.style.backgroundColor = '#006aff';
        }
    });
});





// 리뷰 file 선택시 그 파일을 보여줘야 함. 
let reviewInputImg = document.querySelector('#review-input-img');
let reviewFile = document.querySelector('#review-file');

if(reviewFile != null){
	reviewFile.addEventListener('change',function(e){
		
		let file = e.target.files[0];
		
		let reader = new FileReader();
		reader.readAsDataURL(file);
		
		reader.onload = function(e){
			reviewInputImg.src = e.target.result; 
			reviewInputImg.style.display = 'block';
		}
	})	
}


// x버튼을 클릭하면, —————————————————————————— 
let xBtnIcon = document.querySelector('#x-btn-icon');

if(reviewFile != null){
	let id= reviewFile.id;
	let name = reviewFile.name;
	
}

const reviewWriteDiv1 = document.querySelector('#review-write-div1');

if(xBtnIcon != null){
	xBtnIcon.addEventListener('click', function(e){	
	//1.
	reviewFile.remove();
	reviewFile = document.createElement('input');
	reviewFile.type = 'file';
	reviewFile.id = id;
	reviewFile.name = name;
	reviewFile.style.display = 'none';
	

	//이벤트 리스너도.
	reviewFile.addEventListener('change',function(e){
	
	let file = e.target.files[0];
	
	let reader = new FileReader();
	reader.readAsDataURL(file);
	
	reader.onload = function(e){
		reviewInputImg.src = e.target.result; 
		reviewInputImg.style.display = 'block';
	}
	});
	
	//붙여줘야지 
	reviewWriteDiv1.append(reviewFile);
	
	
	
	reviewInputImg.style.display = 'none';
	});	
}



// 리뷰(부모)의 수정 버튼 클릭 시
document.querySelectorAll('.update-text').forEach(updateText => {
	
	updateText.addEventListener('click', function(e){
		// 뭐 해줘야 해? 
		// 1. 리뷰의 내용(reviewContent)과, 언제 생성된 리뷰인지 나타내는 것(stringCreateDate)을 감춰줘야 함. 
		// 2. 수정폼 보여주기 
		
		let isEditing = this.dataset.editing === 'true';
		let beHide = this.parentElement.previousElementSibling.previousElementSibling;
		let showFormDiv = this.parentElement.previousElementSibling;
		let showForm = this.parentElement.previousElementSibling.children[0];
		let imgInShowForm = this.parentElement.previousElementSibling.children[0].children[1];

		if (!isEditing) {
			// 편집 모드로 전환
			beHide.style.display = 'none';
			showFormDiv.style.display = 'block';
			showForm.style.display = 'flex';
			
			
			this.innerText = '취소';
			
			console.log(showFormDiv.style.display);
			
		} else {
			// 편집 모드 해제
			beHide.style.display = 'block';
			showFormDiv.style.display = 'none';
			showForm.style.display = 'none';
			this.innerText = '수정';
			
			// 신호 지우기
			
			
			// 그리고 무조건 이미지 보여줘야 함. 
			
			// 신호 지우기 (만약, 리뷰의 리플(자식) 에 달려있는 이미지를 지우라는 의미에서 x 버튼을 누른적이 있다면, 그 신호 지워줌)
			if(showForm.lastElementChild.tagName === 'INPUT' && showForm.lastElementChild.name === 'wantDeleteImg'){
				showForm.removeChild(showForm.lastElementChild);
			}
			
			// 그리고, 무조건 이미지 다시 보여줘야함. 
			imgInShowForm.style.display = 'block';
			
		}
		// 상태 토글
		this.dataset.editing = !isEditing;
	});
});

// 리뷰의 수정버튼 눌렀을 때, x버튼 을 누르면 "이미지 감추기"" + "서버에 이미지 지우라는 신호 보내기" 
document.querySelectorAll('.xxx-btn').forEach(xb2 => {
	
	xb2.addEventListener('click', function(e){
		// 이미지 감추기
		this.previousElementSibling.style.display = 'none';
		
		// 서버에 이미지 지우라는 신호 보내기 
		// 1) 일단, 이미 신호가 추가된 적이 있는지를 따짐 
		if(this.parentElement.lastElementChild.tagName === 'INPUT' && this.parentElement.lastElementChild.name === 'wantDeleteImg'){
			return;
		}else{
			// 2) 없을 경우 신호를 추가해줌
			let signal2 = document.createElement('input');
			signal2.type = 'hidden';
			signal2.name = 'wantDeleteImg';
		    signal2.value = 'true';
		    this.parentElement.append(signal2);			
		}		
	});
});





// 댓글 쓰기 버튼 누를 시
document.querySelectorAll('.reply-btn').forEach(replyBtn => {
	
	replyBtn.addEventListener('click', function(e){
		
		let isBool = this.dataset.bool === 'true';
		
		if(isBool){
            replyBtn.nextElementSibling.style.display = 'flex';
		} else{
            replyBtn.nextElementSibling.style.display = 'none';
		}
		
		this.dataset.bool = !isBool;
	})	
});


// 리뷰의 리플(자식) 달 때, 파일 선택하면, 이미지 미리 보여주기 
document.querySelectorAll('.reply-file-input').forEach(replyFileInput => {
	
	replyFileInput.addEventListener('change', function(e){
		
		let replyFilePreviewImg = this.parentElement.nextElementSibling;
		
		let file2 = e.target.files[0];
		let reader = new FileReader();
		reader.readAsDataURL(file2);
		
		reader.onload = function(e){
			replyFilePreviewImg.src = e.target.result;
			replyFilePreviewImg.style.display = 'block';
			
		}
		
	});
	
});


// 리뷰의 리플(자식) 달 때, x 버튼 누르면, 
// 1) 미리 보여줬던 이미지 감추기
// 2) type="file" 인 input 태그를 지우고, 새로 만들어주기 

document.querySelectorAll('.reply-x-btn').forEach(x => {
	
	x.addEventListener('click', function(e){
		
		// 미리 보여줬던 이미지 감추기 
		let previewImg = this.previousElementSibling;
			previewImg.style.display = 'none';
		// type="file" 인 input 태그를 지우고, 새로 만들어주기 
		
		let oldInput = this.previousElementSibling.previousElementSibling.lastElementChild;
		
		let newInput = document.createElement('input');
		newInput.type = 'file';
		newInput.name = 'replyFile';
		newInput.classList.add('reply-file-input reply-file-label'); 
		// reply-file-input 은 display:none; 이라는 style 을 주기 위함이며, 
		// reply-file-label 은 라벨과 연동하기 위함이다. 
		
		// oldInput -> newInput 으로 대체 
		oldInput.parentElement.replaceChild(newInput, oldInput);
		
		
		alert('aaaaaaaaaaaaa');
		
		newInput.addEventListener('change', function(e){
			
			let file3 = e.target.files[0];
			let reader = new FileReader();
			reader.readAsDataURL(file3);
			
			reader.onload = function(e){
			this.parentElement.nextElementSibling.src = e.target.result;
			this.parentElement.nextElementSibling.style.display = 'block';
			
			alert('aawowns');				
			}
		});
		
		
		
	})	
	
});






// 리뷰 리플(자식) 수정버튼 눌렀을 경우 
document.querySelectorAll('.reply-update').forEach(updateBtn => {
	
	updateBtn.addEventListener('click', function(e){
		
		let reupFlag =this.dataset.reup === 'true';
		let beHide2 = this.parentElement.previousElementSibling.previousElementSibling;
		let showFormDiv2 = this.parentElement.previousElementSibling;
		let inFormImg= this.parentElement.previousElementSibling.children[1];
		
		
		if (reupFlag) {
			// 편집 모드로 전환
			beHide2.style.display = 'none';
			showFormDiv2.style.display = 'flex';
			this.innerText = '취소';
			
		} else {
			// 편집 모드 해제
			beHide2.style.display = 'block';
			showFormDiv2.style.display = 'none';
			this.innerText = '수정';
			
			// 신호 지우기 (만약, 리뷰의 리플(자식) 에 달려있는 이미지를 지우라는 의미에서 x 버튼을 누른적이 있다면, 그 신호 지워줌)
			if(showFormDiv2.lastElementChild.tagName === 'INPUT' && showFormDiv2.lastElementChild.name === 'wantDeleteImg'){
				showFormDiv2.removeChild(showFormDiv2.lastElementChild);
			}
			
			// 그리고, 무조건 이미지 다시 보여줘야함. 
			inFormImg.style.display = 'block';
			
			
			
		}
		
		// 상태 토글
		this.dataset.reup = !reupFlag;
	});
	
});


// 리뷰의 리플(자식) 에서 수정버튼 눌렀을 때, x버튼 을 누르면 "이미지 감추기"" + "서버에 이미지 지우라는 신호 보내기" 
document.querySelectorAll('.review-update-x-btn').forEach(xb => {
	
	xb.addEventListener('click', function(e){
		// 이미지 감추기
		this.previousElementSibling.style.display = 'none';
		
		// 서버에 이미지 지우라는 신호 보내기 
		// 1) 일단, 이미 신호가 추가된 적이 있는지를 따짐 
		if(this.parentElement.lastElementChild.tagName === 'INPUT' && this.parentElement.lastElementChild.name === 'wantDeleteImg'){
			return;
		}else{
			// 2) 없을 경우 신호를 추가해줌
			let signal = document.createElement('input');
			signal.type = 'hidden';
			signal.name = 'wantDeleteImg';
		    signal.value = 'true';
		    this.parentElement.append(signal);			
		}
		

	    
		
	});
	
});








//—————————————————————————————————————

// 댓글보기를 누르면 댓글이 보여져야 함. 
document.querySelectorAll('.show-reply-btn').forEach(showb => {
    showb.addEventListener('click', function(e){
		

		

        let count = this.getAttribute('data-dapCount');
        
        let dapgle11 = this.parentElement.nextElementSibling;
        
        
        for(i=0; i<count; i++){
			dapgle11.style.display = (dapgle11.style.display === 'none') ? 'block' : 'none';
			dapgle11 = dapgle11.nextElementSibling;
		}
        

    });
});


//—————————————————————————————————————
// payment
var IMP = window.IMP;
IMP.init("imp46526078");


let quantity = document.querySelector('#quantity').value;

document.querySelector('#want-pay-btn').addEventListener('click', function(e){
	
	
	
	fetch('/payment/checkRestNumber?lectureNo=' + lectureNo + '&quantity=' + quantity + '&selectDate=' + selectDate)
	.then(resp => {
		return resp.text();
	})
	.then(result => {		
		if(result == 0){
			// 재고가 있다는 뜻
			alert('로그인 후 이용해주세요'); 
			
		}
		if(result == 1){
			// 재고가 있다는 뜻
			payContainerShowFlag = true; 
			document.querySelector('#payment-method-container').style.display = 'flex';		

		}
		if(result == 2){
			// 재고가 없다는 뜻 
			alert('남은 자리가 부족합니다');
		}
	})
	
})

document.querySelector('#method-x-div').addEventListener('click', function(e){
	document.querySelector('#payment-method-container').style.display = 'none';
});

let cardPayBtn = document.querySelector('#card-div');


cardPayBtn.addEventListener('click', function(e){
	quantity = document.querySelector('#quantity').value;
	
	// click 하면, fetch 요청
	let obj = {
		'lectureNo' : lectureNo,
		'totalPrice' : totalPrice,
		'quantity' : quantity,
	}
	
	fetch('/payment/addOrder',{
		method : "POST",
		headers : {"Content-Type" : "application/json"},
		body : JSON.stringify(obj),
	})
	.then(resp => {return resp.json();})
	.then(data => {
	
			let merchantUid = data.merchantUid;		

			// 사전 검증 시작 
				let obj2 = {
					'lectureNo' : lectureNo,
					'quantity' : quantity,
					'selectDate' : selectDate,
					'totalPrice' : totalPrice,
					'merchantUid' : merchantUid,
				}
			
			
			fetch('/payment/preValidation', {
				method : "POST",
				headers : {"Content-Type" : "application/json"},
				body : JSON.stringify(obj2),
			})
			.then(resp => {return resp.text()})
			.then(result => {
				if(result.outOfStock != null){
					alert(result.outOfStock);
					return;
				}				
				
				// 사전 검증 완료
				IMP.request_pay(
					// 첫번째 
					{
	                    pg: "html5_inicis",
	                    pay_method: "card",
	                    merchant_uid: merchantUid,
	                    name: lectureTitle,
	                    amount: totalPrice,
	                    buyer_name: '최재준',
	
						
					}, 
					// 두번째 
					function(rsp){
						if(rsp.success){
							fetch('/payment/getPortOneResponse',{
								method : "POST",
								headers : {"Content-Type" : "application/json"},
								body : JSON.stringify({
									imp_uid : rsp.imp_uid,
									merchant_uid : rsp.merchant_uid,
									'lectureNo' : lectureNo,
									'lectureDate' : selectDate,
									'totalPrice' : totalPrice,
								}),
							})
							.then(resp => {return resp.text()})
							.then(data => {
								if(data == 'success'){
									alert('결제가 성공하였습니다.');
								} else{
									alert(data);
								}
								location.href='/lecture/showLectureDetail?lectureNo=' + lectureNo;								
							})
						} else{

							alert('결제에 실패하였습니다. 에러 내용 : ' + rsp.error_msg);
							location.href= '/payment/paymentFail?merchantUid=' + merchantUid + '&impUid=' + rsp.imp_uid + "&selectDate=" +selectDate +'&lectureNo=' +lectureNo+ '&quantity=' + quantity + '&lectureOrdersNo=' + lectureOrdersNo;
						}
					}
				)
			})
		
		
	})
	
})

//——————————————————————————————————————————————
//kakao pay
document.querySelector('#kakao-div').addEventListener('click', function(){
	IMP.request_pay(
	  {
	    pg: "kakaopay",
	    pay_method: "card", // 생략가
	    merchant_uid: "order_no_0004", // 상점에서 생성한 고유 주문번호
	    name: "주문명:결제테스트",
	    amount: 1,
	    buyer_email: "wowns590@naver.com",
	    buyer_name: "최재준",
	    buyer_tel: "010-1234-5678",
	    buyer_addr: "서울특별시 강남구 삼성동",
	    buyer_postcode: "123-456",
	    m_redirect_url: "{모바일에서 결제 완료 후 리디렉션 될 URL}",
	  },
	  function (rsp) {
	    // callback 로직
	    /* …중략… */
	  },
	);

	
})













































