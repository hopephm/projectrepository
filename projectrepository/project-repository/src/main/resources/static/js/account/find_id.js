const find_id = document.querySelector(".find_id"),
    email_input = find_id.querySelector("input[name=user_email]"),
    find_btn = find_id.querySelector(".find_btn");

const find_id_result = document.querySelector(".find_id_result");

function hideFindForm(){
    find_id.style.display="none";
}

function showIds(id_list){
    const id_ul = document.createElement("ul");

    id_list.forEach((value,index)=>{
        const id_li = document.createElement("li");
        id_li.innerHTML=id_list[index];
        id_ul.appendChild(id_li);
    });

    find_id_result.appendChild(id_ul);
    find_id_result.style.display="block";
}

function findBtnOnClickHandler(event){
    var email_val = email_input.value;

    var data = {
        user_email: email_val
    }

    $.ajax({
       url:"/api/account/users/ids",
       data: data,
       type:"GET",
       success:function(res){
            const json = JSON.parse(res);
            const res_code = json["code"];

            if(res_code == SUCCESS){
                const id_list = json["data"]["id_list"];
                alert("아이디 조회완료");
                hideFindForm();
                showIds(id_list);
            }else{
                alert("아이디 조회에 실패하였습니다.\n 처음부터 다시시도 해주세요.");
                window.location.href="/find/id";
            }
       }
   });
}


function init(){
    find_btn.addEventListener("click", findBtnOnClickHandler);
}

init();