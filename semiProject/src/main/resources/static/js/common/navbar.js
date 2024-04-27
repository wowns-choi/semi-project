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

document.addEventListener("DOMContentLoaded", function() {
    var welcomeElement = document.getElementById('welcome');
    var dropdownMenu = document.getElementById('update-user-info-and-logout-dropdown');

    // 마우스가 #welcome 위로 오면 드롭다운 메뉴를 보여줍니다.
    welcomeElement.addEventListener('mouseenter', function() {
        dropdownMenu.style.display = 'flex';  // display 속성을 flex로 설정하여 보이게 합니다.
    });
    
        // 마우스가 #welcome 위로 오면 드롭다운 메뉴를 보여줍니다.
    dropdownMenu.addEventListener('mouseenter', function() {
        dropdownMenu.style.display = 'flex';  // display 속성을 flex로 설정하여 보이게 합니다.
    });

    // 마우스가 #welcome에서 벗어나면 드롭다운 메뉴를 숨깁니다.
    welcomeElement.addEventListener('mouseleave', function() {
        dropdownMenu.style.display = 'none';  // display 속성을 none으로 설정하여 숨깁니다.
    });
    
          // 마우스가 #welcome 위로 오면 드롭다운 메뉴를 보여줍니다.
    dropdownMenu.addEventListener('mouseleave', function() {
        dropdownMenu.style.display = 'none';  // display 속성을 flex로 설정하여 보이게 합니다.
    });
});
