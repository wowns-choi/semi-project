const selectDateSpan = document.querySelector('#select-date-span');


document.addEventListener('DOMContentLoaded', function() {
  var calendarEl = document.getElementById('calendar');
  var calendar = new FullCalendar.Calendar(calendarEl, {
    locale: 'ko',
	initialView: 'dayGridMonth',
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


// 리플 textarea에 입력하기 시작하면 '리뷰' 버튼 색깔 바꾸기
var textarea = document.getElementById('write-content');

let reviewWriteBtn = document.querySelector('#review-write-btn');

textarea.addEventListener('input', function(){
	if(textarea.value == ''){
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

let updateFlag = 0;
let updateFlag2 = 0;
let addInput;
// 리뷰 수정
document.querySelectorAll('.update-text').forEach(updateText => {
	updateText.addEventListener('click', function(e){
		
		let updateFormDiv = this.parentElement.previousElementSibling;
		let reviewImg2 = this.parentElement.previousElementSibling.previousElementSibling;
		let createDate = this.parentElement.previousElementSibling.previousElementSibling.previousElementSibling;
		let currentReviewContent = this.parentElement.previousElementSibling.previousElementSibling.previousElementSibling.previousElementSibling;
		
		// x 버튼 누르면.
		let updateForm = this.parentElement.previousElementSibling.firstElementChild;
		let textarea = this.parentElement.previousElementSibling.firstElementChild.children[0];
		let imgTag= this.parentElement.previousElementSibling.firstElementChild.children[1];
		let xButton= this.parentElement.previousElementSibling.firstElementChild.children[2];
		let saveBtn= this.parentElement.previousElementSibling.firstElementChild.children[3];
						
		updateFlag += 1;
		if(updateFlag % 2 == 0){
			// 수정 취소버튼을 누를 경우, 
			// 다시, 이미지를 block 으로보여주고, 
			imgTag.style.display = '';
			updateFlag2 = 0;
			addInput.remove();
		}
		
		
		xButton.addEventListener('click', function(e){
			if(updateFlag2 == 0){
				imgTag.style.display = 'none';
				addInput = document.createElement('input');
				addInput.type = 'hidden';
				addInput.name = 'wantDeleteImg';
				addInput.value = 'yes';
				updateForm.append(addInput);
				updateFlag2 += 1;				
			}

		});


	
		let flag = updateFormDiv.style.display === 'block' ? true : false;
		
		if(flag == false){
			//none(안보이고 있던 상태)
			updateFormDiv.style.display = 'block';
			currentReviewContent.style.display='none';
			createDate.style.display='none';
			reviewImg2.style.display = 'none';
			this.innerText = '취소';
			
		}else{
			//block(보이고 있던 상태)
			updateFormDiv.style.display = 'none';
			currentReviewContent.style.display='block';	
			createDate.style.display='block';	
			reviewImg2.style.display = 'block';	
			this.innerText = '수정';
		}
		
		
		// x버튼을 클릭했을 경우, 
		
	});
});


// 답글 입력 시작하면, '답글' 버튼 색깔바꾸기
document.querySelectorAll('.dapgle-textarea').forEach(dapgleTextarea =>{
	dapgleTextarea.addEventListener('input', function(e){
		
		let dapgleBtn = dapgleTextarea.nextElementSibling;
		
		if(dapgleTextarea.value == ''){
			dapgleBtn.style.backgroundColor = 'lightgray';
			dapgleBtn.style.color = 'gray';		
		} else{
			dapgleBtn.style.backgroundColor = '#006aff';
			dapgleBtn.style.color = 'white';
		}
	});
});

// 답글 file 선택시 그 파일을 보여줘야 함.
document.querySelectorAll('.reply-file-input').forEach(replyFileInput => {
	replyFileInput.addEventListener('change', function(e){
		let replyFileImg = replyFileInput.parentElement.nextElementSibling;
		let file = e.target.files[0];
	
		let reader = new FileReader();
		reader.readAsDataURL(file);
	
		reader.onload = function(e){
		replyFileImg.src = e.target.result;
		replyFileImg.style.display = 'block';
		
	}})
});



// x버튼을 클릭하면, ---------------------------------------------------- 
document.querySelectorAll('.reply-x-btn').forEach(replyXBtn => {
	
	replyXBtn.addEventListener('click', function(e){
		let replyFileInput = replyXBtn.previousElementSibling.previousElementSibling.firstElementChild;
		let name = replyFileInput.name;
		replyFileInput.remove();
		replyFileInput = document.createElement('input');
		replyFileInput.type = 'file';
		replyFileInput.name = name;
		replyFileInput.classList.add('reply-file-input');
		
		replyFileInput.addEventListener('change',function(e){
			let file = e.target.files[0];
			
			let reader = new FileReader();
			
			reader.readAsDataURL(file);
			
			reader.onload = function(e){
				
        		let replyImg = replyXBtn.previousElementSibling; // 이미지 태그 참조
				
				replyImg.src = e.target.result; 
				replyImg.style.display = 'block';
			}
		});
		
		let replyFileLabel = this.previousElementSibling.previousElementSibling;
		replyFileLabel.append(replyFileInput);
		
		let replyFileImage = this.previousElementSibling;
		replyFileImage.style.display = 'none';
		
	})
});




let addInput2;
let updateFlag3 = 0;
document.querySelectorAll('.reply-update-form>i:nth-child(3)').forEach(x => {
    x.addEventListener('click', function(e) {
        let aa = x.previousElementSibling;  // 이벤트 리스너 내부로 이동

        // 현재 display 상태에 따라 토글합니다.
        if (aa.style.display === 'flex') {
            aa.style.display = 'none';
        } else {
            aa.style.display = 'flex';  // 이전에 누락된 부분을 수정
            
        }
        
        if(updateFlag3 % 2 == 0){
            addInput2 = document.createElement('input');
            addInput2.type = 'hidden';
            addInput2.name = 'wantDeleteImg';
            addInput2.value = 'yes';
           
            this.append(addInput2);
            updateFlag3 += 1;
		}else{
			addInput2.remove();
		}
        
    });
});

let toggleFlag = 0;
document.querySelectorAll('.reply-update').forEach(replyUpdate => {
	replyUpdate.addEventListener('click', function(e){
		let form = this.parentElement.previousElementSibling;
		let img = this.parentElement.previousElementSibling.previousElementSibling;
		let createDate = this.parentElement.previousElementSibling.previousElementSibling.previousElementSibling;
		let content = this.parentElement.previousElementSibling.previousElementSibling.previousElementSibling.previousElementSibling;
		let replyUpdateImg = this.parentElement.previousElementSibling.children[1];
		
		if(toggleFlag%2 == 0){
			form.style.display = 'flex';
			img.style.display = 'none';
			createDate.style.display = 'none';
			content.style.display = 'none';
			this.innerText = '취소';
			toggleFlag += 1;

		} else{
			form.style.display = 'none';
			img.style.display = 'block';
			createDate.style.display = 'block';
			content.style.display = 'block';
			toggleFlag += 1;
			this.innerText = '수정';
			replyUpdateImg.style.display = 'flex';
			if(addInput2 != null){
			addInput2.remove();				
			}


			
		}
		
	});
});









