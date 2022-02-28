const findPw = document.querySelector(".find_pw"),
	sendResetMailBtn = findPw.querySelector(".send_reset_mail_btn"),
	idInput = findPw.querySelector("input[name=user_id]"),
	emailInput = findPw.querySelector("input[name=user_email]");

function sendResetMailBtnOnClickHandler(event){
	var id_val = idInput.value;
	var email_val = emailInput.value;
	var data = {
		user_id: id_val,
		user_email: email_val
	};

	sendResetMailBtn.disabled = true;

	$.ajax({
		url: "/api/account/users/passwords/find",
		data: data,
		type: "POST",
		success:function(res){
            const json = JSON.parse(res);
            const res_code = json["code"];

            if(res_code==SUCCESS){
                alert("비밀번호 재설정 메일을 전송하였습니다.");
            }
			else{
			    alert("비밀번호 재설정 메일을 전송에 실패하였습니다.");
			}

		}
	});
}


function init(){
	sendResetMailBtn.addEventListener("click", sendResetMailBtnOnClickHandler);
}

init();