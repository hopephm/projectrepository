const nickname =  document.querySelector(".nickname"),
    nickname_input = nickname.querySelector("input[name=user_nickname]"),
	check_nickname_btn = nickname.querySelector(".check_nickname");

const id = document.querySelector(".id"),
    id_input = id.querySelector("input[name=user_id]"),
    check_id_btn = id.querySelector(".check_id_btn");

const pw = document.querySelector(".password"),
    pw_input = pw.querySelector("input[name=user_password]"),

    check_pw = document.querySelector(".check_password"),
    check_pw_input = check_pw.querySelector("input[name=repeat_password]"),
    check_pw_btn = check_pw.querySelector(".check_password_btn");

const email = document.querySelector(".email"),
    email_input = email.querySelector("input[name=user_email]"),
    check_email_btn = email.querySelector(".check_email_btn");

const verify = document.querySelector(".verify"),
    verify_input = verify.querySelector("input[name=verify_code]"),
    check_code_btn = verify.querySelector(".check_code_btn");

const join_btn = document.querySelector(".join_btn");

var attr_check = "check";
var fail = "fail";
var success = "success";

function status_init(){
    nickname.setAttribute(attr_check, fail);
    id.setAttribute(attr_check, fail);
    pw.setAttribute(attr_check, fail);
//    email.setAttribute(attr_check, fail);
}

function duplicated_check(item_element, value, item_name, check_url){
        if(value === ""){
            alert(`${item_name}을(를) 입력해주세요`);
        }else{
            $.ajax({
                url:check_url,
                data:"value=" + value,
                type:"GET",
                success:function(data){
                    if(data === success){
                        alert(`사용 가능한 ${item_name}입니다.`);
                        item_element.setAttribute(attr_check, success);
                    }else if(data === fail){
                        alert(`이미 존재하는 ${item_name}입니다.`);
                    }else{
                        alert("오류");
                    }
                }
            });
        }
}

function nicknameCheckBtnOnClickHandler(event){
    var val = nickname_input.value;


    duplicated_check(nickname, val, "닉네임", "/user/duplicated/nickname");
}

function idCheckBtnOnClickHandler(event){
    var val = id_input.value;


    duplicated_check(id, val, "아이디", "/user/duplicated/id");
}

function pwCheckBtnOnClickHandler(event){
    var pw_val = pw_input.value;
    var check_pw_val = check_pw_input.value;

    if(pw_val === ""){
        alert("비밀번호를 입력해주세요");
    }else if(check_pw_val === ""){
        alert("비밀번호를 확인해주세요");
    }else if(pw_val === check_pw_val){
        alert("비밀번호가 일치합니다.");
        pw.setAttribute(attr_check, success);
    }else{
        alert("비밀번호를 확인해주세요");
    }
}

function emailCheckBtnOnClickHandler(event){
    var email_val =email_input.value;
    var data_val = {
        user_email: email_val
    };

    $.ajax({
        url:"/verify/email/send",
        data:data_val,
        type:"POST",
        success:function(data){
            if(data === success){
                alert(`메일로 인증코드를 전송하였습니다.\n 인증코드를 입력해주세요.`);
                verify.style.display="";
            }else if(data === fail){
                alert(`메일 전송에 실패하였습니다.`);
            }else{
                alert("오류");
            }
        }
    });
}

function verifyCodeCheckBtnOnClickHandler(event){
    var email_val = email_input.value;
    var code_val = verify_input.value;
    var data_val = {
        user_email: email_val,
        verify_code: code_val
    };

    $.ajax({
        url:"/verify/email/verify",
        data:data_val,
        type:"POST",
        success:function(data){
            if(data === success){
                alert(`이메일 인증이 완료되었습니다.`);
                email.setAttribute(attr_check, success);
                verify.style.display="none";
            }else if(data === fail){
                alert(`인증코드가 일치하지 않습니다.`);
            }else{
                alert("오류");
            }
        }
    });
}

function joinBtnOnClickHandler(event){
    var nickname_val= nickname_input.value;
    var id_val = id_input.value;
    var pw_val = pw_input.value;
    var email_val = email_input.value;

    if(nickname.getAttribute(attr_check) !== success)
        alert("닉네임 중복검사를 해주세요");
    else if(id.getAttribute(attr_check) !== success)
        alert("아이디 중복검사를 해주세요");
    else if(pw.getAttribute(attr_check) !== success)
        alert("비밀번호를 확인해주세요");
    else if(email.getAttribute(attr_check) !== success)
        alert("이메일을 인증해주세요");
    else{
        var data_val = {
            user_id: id_val,
            user_password: pw_val,
            user_email: email_val,
            user_nickname: nickname_val
        };

        $.ajax({
           url:"/create_account",
           data: data_val,
           type:"POST",
           success:function(data){
                if(data === success){
                    alert("회원가입이 완료되었습니다");
                    window.location.href="/";
                }else{
                    alert("회원가입에 실패했습니다\n 처음부터 다시 시도해주세요");
                    window.location.href="/create_account";
                }
           }
       });
    }
}

function init(){
    status_init();
	check_nickname_btn.addEventListener("click", nicknameCheckBtnOnClickHandler);
	check_id_btn.addEventListener("click", idCheckBtnOnClickHandler);
	check_pw_btn.addEventListener("click", pwCheckBtnOnClickHandler);
	check_email_btn.addEventListener("click", emailCheckBtnOnClickHandler);
	check_code_btn.addEventListener("click", verifyCodeCheckBtnOnClickHandler);
	join_btn.addEventListener("click", joinBtnOnClickHandler);
}

init();