const messageSend = document.querySelectorAll(".message-send");

const popupLayer = document.querySelector("#popupLayer");
const popupMessageNo = document.querySelector("#popupMessageNo");
const popupMessageTitle = document.querySelector("#popupMessageTitle");
const popupClose = document.querySelector("#popupClose");
const popupCheck = document.querySelector("#popupCheck");
const popupRegDate = document.querySelector("#popupRegDate");
const popupMessageContent = document.querySelector("#popupMessageContent");

const updateView = document.querySelector("#updateView");
const deleteBtn = document.querySelector("#deleteBtn");

const updateLayer = document.querySelector("#updateLayer");
const updateTitle = document.querySelector("#updateTitle");
const updateContent = document.querySelector("#updateContent");
const updateBtn = document.querySelector("#updateBtn");
const updateCancel = document.querySelector("#updateCancel");

let memberNo = 0;
let registeredMemberNo = 0;

document.querySelectorAll('.message-send').forEach(function(messageLink) {
        // 각 링크에 클릭 이벤트 리스너 추가
        messageLink.addEventListener('click', function(event) {
        // 이벤트의 기본 동작을 중지
        event.preventDefault();
        popupMessageNo.innerText = "";
        popupMessageTitle.innerText = "";
        popupCheck.innerText = "";
        popupRegDate.innerText = "";
        popupMessageContent.innerText = "";
        
        var memberNoElement = event.target.closest('.point-div').querySelector('.get-member');
        var registeredNoElement = event.target.closest('.point-div').querySelector('.registered-member');

        registeredMemberNo = registeredNoElement.innerText;

        console.log(registeredMemberNo);

        memberNo = memberNoElement.innerText;

        fetch("/register/selectMessage", {
            method : "POST",
            headers : {"Content-Type" : "application/json"},
            body : registeredMemberNo
        })
        .then(resp => resp.json())
        .then(message => {

            if(message.messageNo == 0) {
                alert("메세지를 작성해주세요.");
                popupLayer.classList.remove("popup-hidden");
                updateLayer.classList.add("popup-hidden");

            } else {
                popupMessageNo.innerText = message.messageNo;
                popupMessageTitle.innerText = message.messageTitle;
                popupCheck.innerText = message.messageCheck;
                popupRegDate.innerText = message.messageRegdate;
                popupMessageContent.innerText = message.messageContent;
                popupLayer.classList.remove("popup-hidden");
                updateLayer.classList.add("popup-hidden");

            }

        })
    });
}); 

popupClose.addEventListener("click", () => {
    popupLayer.classList.add("popup-hidden");
});

if(updateView != null) {

    updateView.addEventListener("click", () => {

        popupLayer.classList.add("popup-hidden");

        updateLayer.classList.remove("popup-hidden");
    
        updateTitle.value = popupMessageTitle.innerText;
        updateContent.value = popupMessageContent.innerHTML.replaceAll("<br>", "\n");

        updateBtn.setAttribute("data-message-no", popupMessageNo.innerText);
    });

    updateCancel.addEventListener("click", () => {

        updateLayer.classList.add("popup-hidden");

        popupLayer.classList.remove("popup-hidden");
    });
}

if(updateBtn != null) {
    updateBtn.addEventListener("click", e => {

        console.log(popupMessageNo.innerText);

        const obj = {
            "messageNo" : popupMessageNo.innerText,
            "memberNo" : memberNo,
            "messageTitle" : updateTitle.value,
            "messageContent" : updateContent.value,
            "registeredMemberNo" : registeredMemberNo
        };

        fetch("/register/update", {
            method : "POST",
            headers : {"Content-Type" : "application/json"},
            body : JSON.stringify(obj)
        })
        .then(resp => resp.json())
        .then(message => {
    
            if(message.messageNo != 0) {
                alert("메세지가 전송되었습니다.");
                updateLayer.classList.add("popup-hidden");
    
                popupMessageNo.innerText = message.messageNo;
                popupMessageTitle.innerText = message.messageTitle;
                popupCheck.innerText = message.messageCheck;
                popupRegDate.innerText = message.messageRegdate;
                popupMessageContent.innerText = message.messageContent;

                popupLayer.classList.remove("popup-hidden");

                updateBtn.removeAttribute("data-todo-no");
            } else {
                alert("메세지 전송에 실패하였습니다.");
            }
            
        })

    });
}

if(deleteBtn != null) {
    deleteBtn.addEventListener("click", () => {
        // if(!confirm("정말 삭제하시겠습니까?")) {
        //     
        // }

        // if(confirm("정말 삭제하시겠습니까? ()")) {
        //     fetch("/register/delete", {
        //         method : "DELETE",
        //         headers : {"Content-Type" : "application/json"},
        //         body : messageNo
        //     })
        //     .then(resp => resp.text())
        //     .then(result => {
        //         if(result > 0) {
        //             alert("삭제 성공");
        //             popupLayer.classList.add("popup-hidden");
    
        //         } else {
        //             alert("삭제 실패");
        //         }
        //     });
        // } else {
            
        // }
        
        const messageNo = popupMessageNo.innerText;
        
        if(popupCheck.innerText == 'N') {
            if(!confirm("정말 삭제하시겠습니까? (확인 클릭 시 상대방에게서도 메세지가 삭제됩니다.)")) {
                // 취소 클릭시 강사쪽에서만 안보이게
                fetch("/register/onesideDelete", {
                    method : "POST",
                    headers : {"Content-Type" : "application/json"},
                    body : messageNo
                })
                .then(resp => resp.text())
                .then(result => {
                    if(result > 0) {
                        alert("메세지가 삭제되었습니다.");
                        popupLayer.classList.add("popup-hidden");
                    } else {
                        alert("삭제 실패");
                    }
                })
    
            }
    
            fetch("/register/delete", {
                method : "DELETE",
                headers : {"Content-Type" : "application/json"},
                body : messageNo
            })
            .then(resp => resp.text())
            .then(result => {
                if(result > 0) {
                    alert("삭제 성공");
                    popupLayer.classList.add("popup-hidden");
    
                } else {
                    alert("삭제 실패");
                }
            });

        }

        if(!confirm("정말 삭제하시겠습니까?")) {
            return;
        }

        fetch("/register/onesideDelete", {
            method : "POST",
            headers : {"Content-Type" : "application/json"},
            body : messageNo
        })
        .then(resp => resp.text())
        .then(result => {
            if(result > 0) {
                alert("메세지가 삭제되었습니다.");
                popupLayer.classList.add("popup-hidden");
            } else {
                alert("삭제 실패");
            }
        });

    });
}