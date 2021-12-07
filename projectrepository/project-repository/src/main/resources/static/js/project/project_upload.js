// 프로젝트 추가 후 조회로 redirect
const project = document.querySelector(".project"),
    title_input = project.querySelector("input[name=title]"),
    subject_input = project.querySelector("input[name=subject]"),
    techstack_input = project.querySelector("input[name=techstack]"),
    scale_input = project.querySelector("input[name=scale]"),
    start_date_input = project.querySelector("input[name=start_date]"),
    end_date_input = project.querySelector("input[name=end_date]"),

    content_ul = project.querySelector(".content_list"),
    content_last_li = project.querySelector(".last_content"),
    content_last_btn = content_last_li.querySelector("button"),

    subtitle_holder = project.querySelector(".subtitle_holder"),
    file_holder = project.querySelector(".file_holder"),
    content_holder = project.querySelector(".content_holder"),

    content_title = project.querySelector("input[name=contentTitle]"),
    content_file = project.querySelector("input[name=file]"),
    content_content = project.querySelector("textarea[name=content]"),

    upload_btn = project.querySelector(".upload"),
    add_btn = project.querySelector(".add_content");

var fail = "fail";
var success = "success";
var content_info = [{title:content_title, file:content_file, content:content_content}];
var current_content = 0;

function uploadBtnOnClickHandler(event){
    const title_val = title_input.value;
    const subject_val = subject_input.value;
    const techstack_val = techstack_input.value;
    const scale_val = scale_input.value;
    const start_date_val = start_date_input.value;
    const end_date_val = end_date_input.value;

    var data = {
        title:title_val,
        subject: subject_val,
        techstack: techstack_val,
        scale: scale_val,
        startDate: start_date_val,
        endDate: end_date_val
    };

    var formData = new FormData();

    var contentDTOS = [];

    for(let i = 0; i < content_info.length; i++){
        var contentDTO={};
        contentDTO["contentTitle"] = content_info[i]["title"].value;
        contentDTO["content"] = content_info[i]["content"].value;
        contentDTO["no"] = i;
        
        if(content_info[i]["file"].value){
            var file_element = content_info[i]["file"].files[0];
            formData.append("file", file_element);
        }else{
            contentDTO["fileName"] = "null";
            formData.append("file", new File([" "], "__dummy", {type: "text/plain",}));
        }
        
        contentDTOS.push(contentDTO);
    }
    
    data["contentDTOS"] = contentDTOS;

    formData.append('projectDTO', new Blob([JSON.stringify(data)] , {type: "application/json"}));

     if(title_val == "") alert("제목을 입력해주세요.");
     else if(subject_val == "") alert("주제를 입력해주세요.");
     else if(techstack_val == "") alert("기술스택을 입력해주세요.");
     else if(scale_val == "") alert("규모를 입력해주세요.");
     else if(start_date_val == "") alert("프로젝트 시작날짜를 입력해주세요.");
     else if(end_date_val == "") alert("프로젝트 종료날짜를 입력해주세요.");
     else{
        var url="/upload";

        $.ajax({
            url:url,
            data: formData,
            processData: false,
            contentType: false,
            type:"POST",
            success:function(data){
                alert(`프로젝트가 업로드 되었습니다.`);
                window.location.href = `/project/${data}`;
            }
        });
     }
}

function changeTo(od){
    while(subtitle_holder.hasChildNodes()){
        subtitle_holder.removeChild(subtitle_holder.firstChild);
    }
    subtitle_holder.appendChild(content_info[od]["title"]);

    while(file_holder.hasChildNodes()){
        file_holder.removeChild(file_holder.firstChild);
    }
    file_holder.appendChild(content_info[od]["file"]);

    while(content_holder.hasChildNodes()){
        content_holder.removeChild(content_holder.firstChild);
    }
    content_holder.appendChild(content_info[od]["content"]);

    current_content = od;
}

function summarize(str){
    if(str.length > 5)
        return str.substring(0,5) + "..";
    return str;
}

function createNewContent(){
    // 원래 항목을 저장, 해당 버튼 생성
    var content_li = document.createElement("li");
    var content_btn = document.createElement("button");

    var cur_title = content_info[current_content]["title"];

    if(cur_title.value == "")
        content_btn.innerText = `항목 ${content_info.length}`;
    else{
       content_btn.innerText = summarize(cur_title.value);
    }

    // 새로 생성하는 항목의 순서 표시
    content_last_li.setAttribute("order", parseInt(content_last_li.getAttribute("order")) +1);

    content_btn.setAttribute("type", "button");
    content_btn.classList.add("subtitle");
    content_btn.addEventListener("click", subtitleBtnOnClickHandler);

    content_li.classList.add("horizontal");
    content_li.setAttribute("order", content_info.length-1);
    content_li.appendChild(content_btn);

    content_ul.insertBefore(content_li, content_last_li);

    var ntitle = document.createElement("input");
        ntitle.setAttribute("type", "text");
        ntitle.setAttribute("name", "contentTitle");
        ntitle.addEventListener("input", subtitleInputOnChangeHandler);
    var ncontent = document.createElement("textarea");
        ncontent.setAttribute("name", "content");
        ncontent.setAttribute("cols", 50);
        ncontent.setAttribute("rows", 10);
    var nfile = document.createElement("input");
        nfile.setAttribute("type", "file");
        nfile.setAttribute("name", "file");

    content_info.push({title: ntitle, content: ncontent, file: nfile});

    content_last_btn.innerText = "새 항목";
}


function subtitleBtnOnClickHandler(event){
    var li = event.currentTarget.parentElement;
    var od = parseInt(li.getAttribute("order"));

    changeTo(od);
}

function subtitleInputOnChangeHandler(event){
    var t = event.currentTarget.value;
    var btn = content_ul.querySelector(`li:nth-child(${current_content+1}) > button`);
    btn.innerText = summarize(t);
}

function addBtnOnClickHandler(event){
    // 현재 값이 저장되어야 함
    createNewContent();

    changeTo(content_info.length-1);
}

function init(){
    upload_btn.addEventListener("click", uploadBtnOnClickHandler);
    add_btn.addEventListener("click", addBtnOnClickHandler);
    content_last_btn.addEventListener("click", subtitleBtnOnClickHandler);
    content_title.addEventListener("input", subtitleInputOnChangeHandler);
}

init();