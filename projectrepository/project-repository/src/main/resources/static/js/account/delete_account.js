const yes_btn = document.querySelector(".yes_btn"),
    no_btn = document.querySelector(".no_btn");

function yesBtnOnClickHandler(event){
    var url = "/delete_account";

    $.ajax({
        url: url,
        type:"POST",
        success:function(data){
            alert("회원탈퇴가 완료되었습니다.");
            window.location.href = "/";
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

