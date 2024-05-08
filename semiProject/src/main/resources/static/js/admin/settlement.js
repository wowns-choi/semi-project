let settledBtn = document.querySelector('#settled-btn');
let unsettledBtn =  document.querySelector('#unsettled-btn');
let countBtn = document.querySelector('#count-btn');


settledBtn.addEventListener('click' , function(){
	countBtn.value = 1;	
})

unsettledBtn.addEventListener('click' , function(){
	countBtn.value = -1;
})

window.onload = function() {
	if(countBtn.value== 1){
		settledBtn.style.color = 'red';
	}else if (countBtn.value== -1){
		unsettledBtn.style.color = 'red';
	}
};
settledBtn.addEventListener('click', function(){
	settledBtn.style.color = 'red';
	unsettledBtn.style.color = 'black';

})

unsettledBtn.addEventListener('click', function(){
	unsettledBtn.style.color = 'red';
	settledBtn.style.color = 'black';

})

