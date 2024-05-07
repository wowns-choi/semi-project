const mainFlag = document.querySelector('#main-flag');
let beforeImg1 = document.querySelector('#before-img1');
let afterImg1 = document.querySelector('#after-img1');	
let addNew1 = document.querySelector('#add-new-1');

// 이미지 미리보기 
addNew1.addEventListener('change', function(e){
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function(e){
		afterImg1.src = e.target.result;
		afterImg1.style.display = 'block';
		beforeImg1.style.display = 'none';
	}
	mainFlag.value = 1;
});

document.querySelector('#x1').addEventListener('click', function(e){
	// x버튼 클릭하면, 무조건 기존 이미지 삭제한다는 의미가 되도록 설계할거임. 
	mainFlag.value = -1;
	
	beforeImg1.style.display = 'none';
	afterImg1.style.display = 'none';
});

//----------------------------------------------------------------------------------

const sub1Flag = document.querySelector('#sub1-flag');
let beforeImg2 = document.querySelector('#before-img2');
let afterImg2 = document.querySelector('#after-img2');	
let addNew2 = document.querySelector('#add-new-2');

// 이미지 미리보기 
addNew2.addEventListener('change', function(e){
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function(e){
		afterImg2.src = e.target.result;
		afterImg2.style.display = 'block';
		if(beforeImg2 != null){
		beforeImg2.style.display = 'none';			
		}

	}
	sub1Flag.value = 1;
});

document.querySelector('#x2').addEventListener('click', function(e){
	// x버튼 클릭하면, 무조건 기존 이미지 삭제한다는 의미가 되도록 설계할거임. 
	sub1Flag.value = -1;
	if(beforeImg2 != null){
		beforeImg2.style.display = 'none';		
	}

	afterImg2.style.display = 'none';
});
//----------------------------------------------------------------------------------

const sub2Flag = document.querySelector('#sub2-flag');
let beforeImg3 = document.querySelector('#before-img3');
let afterImg3 = document.querySelector('#after-img3');	
let addNew3 = document.querySelector('#add-new-3');

// 이미지 미리보기 
addNew3.addEventListener('change', function(e){
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function(e){
		afterImg3.src = e.target.result;
		afterImg3.style.display = 'block';
		if(beforeImg3 != null){
		beforeImg3.style.display = 'none';
		}
	}
	sub2Flag.value = 1;
});

document.querySelector('#x3').addEventListener('click', function(e){
	// x버튼 클릭하면, 무조건 기존 이미지 삭제한다는 의미가 되도록 설계할거임. 
	sub2Flag.value = -1;
	if(beforeImg3 != null){
		beforeImg3.style.display = 'none';		
	}
	afterImg3.style.display = 'none';
});
//----------------------------------------------------------------------------------

const sub3Flag = document.querySelector('#sub3-flag');
let beforeImg4 = document.querySelector('#before-img4');
let afterImg4 = document.querySelector('#after-img4');	
let addNew4 = document.querySelector('#add-new-4');

// 이미지 미리보기 
addNew4.addEventListener('change', function(e){
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function(e){
		afterImg4.src = e.target.result;
		afterImg4.style.display = 'block';
		if(beforeImg4 != null){
		beforeImg4.style.display = 'none';			
		}
	}
	sub3Flag.value = 1;
});

document.querySelector('#x4').addEventListener('click', function(e){
	// x버튼 클릭하면, 무조건 기존 이미지 삭제한다는 의미가 되도록 설계할거임. 
	sub3Flag.value = -1;
	if(beforeImg4 != null){
		beforeImg4.style.display = 'none';		
	}
	afterImg4.style.display = 'none';
});

//----------------------------------------------------------------------------------

const sub4Flag = document.querySelector('#sub4-flag');
let beforeImg5 = document.querySelector('#before-img5');
let afterImg5 = document.querySelector('#after-img5');	
let addNew5 = document.querySelector('#add-new-5');

// 이미지 미리보기 
addNew5.addEventListener('change', function(e){
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function(e){
		afterImg5.src = e.target.result;
		afterImg5.style.display = 'block';
		if(beforeImg4 != null){
			beforeImg5.style.display = 'none';
		}
		
	}
	sub4Flag.value = 1;
});

document.querySelector('#x5').addEventListener('click', function(e){
	// x버튼 클릭하면, 무조건 기존 이미지 삭제한다는 의미가 되도록 설계할거임. 
	sub4Flag.value = -1;
	if(beforeImg5 != null){
		beforeImg5.style.display = 'none';
	}	
	
	afterImg5.style.display = 'none';
});





















