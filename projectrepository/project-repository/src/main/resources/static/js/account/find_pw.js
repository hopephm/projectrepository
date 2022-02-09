const findPw = document.querySelector(".find_pw"),
	sendResetMailBtn = findPw.querySelector(".send_reset_mail_btn"),
	idInput = findPw.querySelector("input[name=user_id]"),
	emailInput = findPw.querySelector("input[name=user_email]");

function sendResetMailBtnOnClickHandler(event){
	var id = idInput.value;
	var email = emailInput.value;
	var data = {
		user_id: id,
		user_email: email
	};

	sendResetMailBtn.disabled = true;

	$.ajax({
		url: "/find/pw",
		data: data,
		type: "POST",
		success:function(data){
			alert("비밀번호 재설정 메일을 전송하였습니다.");
		}
	});

}


function init(){
	sendResetMailBtn.addEventListener("click", sendResetMailBtnOnClickHandler);
}

init();