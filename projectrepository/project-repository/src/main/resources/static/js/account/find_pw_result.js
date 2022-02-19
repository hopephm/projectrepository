const reset = document.querySelector(".reset_pw"),
    reset_btn = reset.querySelector(".reset_btn");

const reset_rst = document.querySelector(".reset_pw_result"),
    account_info = reset_rst.querySelector(".account_info"),
    id_td = account_info.querySelector(".id"),
    pw_td = account_info.querySelector(".pw");

function getCurrentParams(){
    const url = new URL(window.location.href);
    return url.searchParams;
}

function resetBtnOnClickHandler(event){
    var params = getCurrentParams();
    var id = params.get("user_id");
    var key = params.get("key");

    var data = {
        user_id: id,
        reset_key: key
    };

    $.ajax({
        url: "/rest/account/users/passwords/reset",
        data: data,
        type: "POST",
        success:function(res){
            const json = JSON.parse(res);
            const res_code = json["code"];

            if(res_code==SUCCESS){
                const id = json["data"]["id"];
                const pw = json["data"]["password"];

                id_td.innerText=id;
                pw_td.innerText=pw;

                reset.style.display = "none";
                reset_rst.style.display="block";
            }else{
                alert("비밀번호 재설정에 실패하였습니다.");
            }
        }
    });
}

function init(){
    reset_btn.addEventListener("click", resetBtnOnClickHandler);
}

init();