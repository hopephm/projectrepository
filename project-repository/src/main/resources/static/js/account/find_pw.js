const find_pw = document.querySelector(".find_pw"),
    id_input = find_pw.querySelector("input[name=user_id]"),
    email_input = find_pw.querySelector("input[name=user_email]"),
    check_account_btn = find_pw.querySelector(".check_account");

const verify = document.querySelector(".verify_account"),
    verify_input = verify.querySelector("input[name=verify_code]"),
    check_code_btn = verify.querySelector(".check_code");

const reset_btn = find_pw.querySelector(".reset_pw");

var fail = "fail";
var success = "success";

function checkAccountBtnOnClickHandler(event){
    var id_val = id_input.value;
    var email_val =email_input.value;

    var data_val = {
        user_id: id_val,
        user_email: email_val
    };

    $.ajax({
        url:"/verify/find/pw",
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

function checkCodeBtnOnClickHandler(event){
    var id_val = id_input.value;
    var code_val = verify_input.value;

    var data_val = {
        user_id: id_val,
        verify_code: code_val
    };

    $.ajax({
        url:"/verify/find/pw/verify",
        data:data_val,
        type:"POST",
        success:function(data){
            if(data === success){
                alert(`계정이 인증되었습니다.`);
                verify.style.display="none";
                reset_btn.style.display="block";
            }else if(data === fail){
                alert(`계정 인증에 실패하였습니다.`);
            }else{
                alert("오류");
            }
        }
    });
}

function resetBtnOnClickHandler(event){
    var id_val = id_input.value;
    var email_val = email_input.value;

    var data_val = {
        user_id: id_val,
        user_email: email_val
    };

    $.ajax({
            url:"/verify/find/pw/reset",
            data:data_val,
            type:"POST",
            success:function(data){
                if(data === success){
                    alert(`메일로 임시 비밀번호가 전송되었습니다.`);
                    window.location.href="/";
                }else if(data === fail){
                    alert(`비밀번호 재설정에 실패하였습니다.`);
                }else{
                    alert("오류");
                }
            }
        });
}


function init(){
    check_account_btn.addEventListener("click", checkAccountBtnOnClickHandler);
    check_code_btn.addEventListener("click", checkCodeBtnOnClickHandler);
    reset_btn.addEventListener("click", resetBtnOnClickHandler);
}

init();