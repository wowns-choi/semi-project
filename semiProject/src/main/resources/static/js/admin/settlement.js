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
		settledBtn.style.color = 'coral';
	}else if (countBtn.value== -1){
		unsettledBtn.style.color = 'coral';
	}
};
settledBtn.addEventListener('click', function(){
	settledBtn.style.color = 'coral';
	unsettledBtn.style.color = 'black';

})

unsettledBtn.addEventListener('click', function(){
	unsettledBtn.style.color = 'coral';
	settledBtn.style.color = 'black';

})

