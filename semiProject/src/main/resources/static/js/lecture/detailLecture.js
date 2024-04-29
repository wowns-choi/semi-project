const selectDateSpan = document.querySelector('#select-date-span');


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
		

		
		
      // 예를 들어, 폼의 날짜 입력 필드에 날짜를 설정
      //document.getElementById('selectedDate').value = info.dateStr;
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
    let totalPrice = pricePerInt * currentQuantity;
    
    document.querySelector('#total-price').innerText = totalPrice + '원';    
}


//====================================================================================

// 리플 textarea에 입력하기 시작하면 '리뷰' 버튼 색깔 바꾸기
var textarea2 = document.getElementById('write-content');

let reviewWriteBtn = document.querySelector('#review-write-btn');

textarea2.addEventListener('input', function(){
	if(textarea2.value == ''){
		reviewWriteBtn.style.backgroundColor = 'lightgray';	
		reviewWriteBtn.style.color = 'gray';	
	} else{
		reviewWriteBtn.style.backgroundColor = '#006aff';	
		reviewWriteBtn.style.color = 'white';	
	}
});

// 리뷰 file 선택시 그 파일을 보여줘야 함. 
let reviewInputImg = document.querySelector('#review-input-img');
let reviewFile = document.querySelector('#review-file');

reviewFile.addEventListener('change',function(e){
	
	let file = e.target.files[0];
	
	let reader = new FileReader();
	reader.readAsDataURL(file);
	
	reader.onload = function(e){
		reviewInputImg.src = e.target.result; 
		reviewInputImg.style.display = 'block';
	}
})

// x버튼을 클릭하면, ---------------------------------------------------- 
let xBtnIcon = document.querySelector('#x-btn-icon');

let id= reviewFile.id;
let name = reviewFile.name;

const reviewWriteDiv1 = document.querySelector('#review-write-div1');

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
			signal2.type = 'text';
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








//--------------------------------------------------------------------------






