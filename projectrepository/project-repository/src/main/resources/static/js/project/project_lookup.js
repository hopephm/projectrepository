// content_holder 에서  display 설정 및 클릭 이벤트 처리
project = document.querySelector(".project"),
    subtitle = project.querySelectorAll(".subtitle"),

//    content_holder = project.querySelector(".content_holder"),
    content = document.querySelectorAll(".content")
;
var current_content = 0;

function subtitleBtnOnClickHandler(event){
    var li = event.currentTarget.parentElement;
    var idx = $(li).index();

    content[current_content].setAttribute("style", "display:none");
    content[idx].setAttribute("style", "display:block");
    current_content = idx;
}

function _init(){
   content[current_content].setAttribute("style", "display:block");
   for(let i = 0; i < subtitle.length; i++){
        subtitle[i].addEventListener("click", subtitleBtnOnClickHandler);
   }
}

function init(){
    _init();
}

init();