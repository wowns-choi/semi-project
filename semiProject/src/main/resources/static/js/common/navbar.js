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
	let imgContainer = document.querySelector('#img-container2');
    var welcomeElement = document.getElementById('welcome');
    var dropdownMenu = document.getElementById('update-user-info-and-logout-dropdown');

    if(imgContainer != null) {
        // 마우스가 #welcome 위로 오면 드롭다운 메뉴를 보여줍니다.
        imgContainer.addEventListener('mouseenter', function() {
            dropdownMenu.style.display = 'flex';  // display 속성을 flex로 설정하여 보이게 합니다.
        });

        // 마우스가 #welcome에서 벗어나면 드롭다운 메뉴를 숨깁니다.
        imgContainer.addEventListener('mouseleave', function() {
            dropdownMenu.style.display = 'none';  // display 속성을 none으로 설정하여 숨깁니다.
        });
    }
   
    
    if(dropdownMenu != null) {
            // 마우스가 #welcome 위로 오면 드롭다운 메뉴를 보여줍니다.
        dropdownMenu.addEventListener('mouseenter', function() {
            dropdownMenu.style.display = 'flex';  // display 속성을 flex로 설정하여 보이게 합니다.
        });

        
        
            // 마우스가 #welcome 위로 오면 드롭다운 메뉴를 보여줍니다.
        dropdownMenu.addEventListener('mouseleave', function() {
            dropdownMenu.style.display = 'none';  // display 속성을 flex로 설정하여 보이게 합니다.
        });

    }
    
});

const messageLayer = document.querySelector("#messageLayer");
const layerMessageNo = document.querySelector("#layerMessageNo");
const layerMessageTitle = document.querySelector("#layerMessageTitle");
const layerClose = document.querySelector("#layerClose");
const lecturerProfile = document.querySelector("#lecturer-profile");
const lecturerProfileImg = document.querySelector("#lecturer-profileImg");
const lecturerNickname = document.querySelector("#lecturer-nickname");
const layerRegDate = document.querySelector("#layerRegDate");
const layerMessageContent = document.querySelector("#layerMessageContent");

const messageIcon = document.querySelector("#message-icon");
const notification = document.querySelector("#notification");

const notiSpan = document.querySelector("#notiSpan");

const layerMessageList = document.querySelector("#layerMessageList");

const selectMessage = (url) => {

    fetch(url)
    .then(resp => resp.json())
    .then(message => {

        if(message.messageNo == 0) {
            alert("메세지 조회 실패");
        } else {
            layerMessageNo.innerText = message.messageNo;
            layerMessageTitle.innerText = message.messageTitle;
            layerRegDate.innerText = message.messageRegdate;
            layerMessageContent.innerText = message.messageContent;
            messageLayer.classList.remove("popup-hidden");
            layerMessageList.classList.add("popup-hidden");
        }

    });
};

if(messageIcon != null) {

    messageIcon.addEventListener("click", () => {
    
        console.log(notiSpan.innerText);
    
        if(notiSpan.innerText == 0) {
            alert("도착한 메세지가 없습니다.");
            return;
        }
    
        // 한개 조회해와서 바로 보여주기
        if(notiSpan.innerText == 1) {
            fetch("/register/showMessage")
            .then(resp => resp.json())
            .then(message => {
                layerMessageNo.innerText = message.messageNo;
                layerMessageTitle.innerText = message.messageTitle;
                lecturerProfileImg.src = message.profileImg;
                lecturerNickname.innerText = message.memberNickname;
                layerRegDate.innerText = message.messageRegdate;
                layerMessageContent.innerText = message.messageContent;
                messageLayer.classList.remove("popup-hidden");
            })
    
        }
    
        if(notiSpan.innerText > 1) {
            fetch("/register/showMessageList")
            .then(resp => resp.json())
            .then(messageList => {
                layerMessageList.innerHTML = "";
    
                const layerCloseSpan = document.createElement('span');
                layerCloseSpan.id = 'layerCloseSpan';
                layerCloseSpan.innerHTML = '&times;';
    
                layerMessageList.appendChild(layerCloseSpan);
    
                for(let message of messageList) {
    
                    const popupRow = document.createElement('div');
                    popupRow.className = 'popup-row';
                    popupRow.id = 'noLine';
    
                    // 번호(span#layerMessageNo) 엘리먼트 생성
                    let messageNo = document.createElement('span');
                    messageNo.id = 'layerMessageNo';
                    popupRow.appendChild(document.createTextNode('번호 : '));
                    messageNo.innerText = message.messageNo;
                    popupRow.appendChild(messageNo);
                    popupRow.appendChild(document.createTextNode(' | '));
    
                    // 제목(span#layerMessageTitle) 엘리먼트 생성
                    let messageTitle = document.createElement('span');
                    messageTitle.id = 'layerMessageTitle';
                    popupRow.appendChild(document.createTextNode('제목 : '));
                    popupRow.appendChild(messageTitle);
    
                    // 제목 내부의 링크(a#layerMessageATag) 엘리먼트 생성
                    let messageLink = document.createElement('a');
                    messageLink.innerText = message.messageTitle;
                    messageLink.id = 'layerMessageATag';
                    messageLink.href = "/register/showMessageHref?messageNo=" + message.messageNo;
                    messageLink.style.textDecoration = 'none';
                    messageLink.style.color = 'black';
                    messageTitle.appendChild(messageLink);
    
                    const popupRower = document.createElement('div');
                    popupRower.className = 'popup-row';
    
                    // 메세지 발송일(span#layerRegDate) 엘리먼트 생성
                    let regDate = document.createElement('span');
                    regDate.id = 'layerRegDate';
                    regDate.innerText = message.messageRegdate;
                    popupRower.appendChild(document.createTextNode('\n'));
                    popupRower.appendChild(document.createTextNode('메세지 발송일 : '));
                    popupRower.appendChild(regDate);
    
                    if(message.messageTitle) {
                         messageLink.addEventListener("click", e => {
                             e.preventDefault();
                             selectMessage(e.target.href);
                         });
                    }
    
                    layerMessageList.appendChild(popupRow);
                    layerMessageList.appendChild(popupRower);
                }
    
                layerMessageList.classList.remove("popup-hidden");
    
                layerCloseSpan.addEventListener("click", () => {
                layerMessageList.classList.add("popup-hidden");
                })
            })
        }
    
        layerMessageList.classList.add("popup-hidden");
    
    });
}

if(layerClose != null) {

    layerClose.addEventListener("click", () => {
        messageLayer.classList.add("popup-hidden");
        if(notiSpan.innerText > 1)
        layerMessageList.classList.remove("popup-hidden");
    });
}

const layerDeleteBtn = document.querySelector("#layerDeleteBtn");

if(layerDeleteBtn != null) {

    layerDeleteBtn.addEventListener("click", () => {
        if(!confirm("정말 삭제하시겠습니까?")) {
            return;
        }

        const messageNo = layerMessageNo.innerText;
        let count = notiSpan.innerText;
        console.log(count);
        fetch("/register/delete", {
            method : "DELETE",
            headers : {"Content-Type" : "application/json"},
            body : messageNo
        })
        .then(resp => resp.text())
        .then(result => {
            if(result > 0) {
                alert("삭제 성공");
                notiSpan.innerText = count -1;

            } else {
                alert("삭제 실패");
            }
        });
        messageLayer.classList.add("popup-hidden");
    })
}