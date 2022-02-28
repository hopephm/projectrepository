const yes_btn = document.querySelector(".yes_btn"),
    no_btn = document.querySelector(".no_btn");

function yesBtnOnClickHandler(event){
    $.ajax({
        url: "/api/account/users",
        type:"DELETE",
        success:function(res){
            const json = JSON.parse(res);
            const res_code = json["code"];

            if(res_code==SUCCESS){
                alert("회원탈퇴가 완료되었습니다.");
                window.location.href = "/";
            }else{
                alert("오류");
            }

        }
    });
}

function noBtnOnClickHandler(event){
    alert("메인화면으로 이동합니다.");
    window.location.href = "/";
}

function init(){
    yes_btn.addEventListener("click", yesBtnOnClickHandler);
    no_btn.addEventListener("click", noBtnOnClickHandler);
}

init();

