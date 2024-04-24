const loginModalBtn = document.querySelector('#show-login-modal'); // navbar.html 에 있다. 
const loginModalContainer = document.querySelector('#login-modal-container'); //loginModal.html 에 있다.

if(loginModalBtn != null){
	loginModalBtn.addEventListener('click', function(e){
		loginModalContainer.style.display = 'block';
	})	
}

// x 버튼 누르면 모달창 닫히도록 해줄거임. 
const loginModalX = document.querySelector('#x-btn');

loginModalX.addEventListener('click', function(e){
	loginModalContainer.style.display = 'none';
	
})
