// lookup: 사용자 검사 및 동일 사용자라면 수정 버튼 활성화      완료

// edit
//    - 수정가능한 사람이 접근했는지 확인              완료
//    - 프로젝트 Id에 해당하는 프로젝트 정보 로드       완료
//    - 각 input에 정보 채움                          완료
//        >> file input 상태에 주의                  남음
//    - 수정 버튼 클릭시 PUT METHOD로 업데이트          남음

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

    sub_title_holder = project.querySelector(".sub_title_holder"),
    file_holder = project.querySelector(".file_holder"),
    content_holder = project.querySelector(".content_holder"),

    content_title = project.querySelector("input[name=contentTitle]"),
    content_file = project.querySelector("input[name=file]"),
    content_content = project.querySelector("textarea[name=content]"),

    edit_btn = project.querySelector(".edit_btn"),
    add_btn = project.querySelector(".add_content");

var content_info = [{title:content_title, file:content_file, content:content_content}];
var current_content = 0;

function getToday(){
    var date = new Date();

    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    var day = ('0' + date.getDate()).slice(-2);

    return year + "-" + month + "-" + day;
}

function checkInput(data){
    var today = getToday();

    if(data["title"] == "") alert("제목을 입력해주세요.");
    else if(data["subject"] == "") alert("주제를 입력해주세요.");
    else if(data["techstack"] == "") alert("기술스택을 입력해주세요.");
    else if(data["scale"] == "") alert("규모를 입력해주세요.");
    else if(data["projectStartDate"] == "") alert("프로젝트 시작날짜를 입력해주세요.");
    else if(today < data["projectStartDate"]) alert("프로젝트 시작날짜를 확인해주세요.");
    else if(data["projectEndDate"] == "") alert("프로젝트 종료날짜를 입력해주세요.");
    else if(today < data["projectEndDate"]) alert("프로젝트 종료날짜를 확인해주세요.");
    else if(data["projectStartDate"] > data["projectEndDate"])
        alert("프로젝트 종료날짜는 시작날짜보다 빠를 수 없습니다.");
    else{
        return true;
    }
    return false;
}

function getOverviewInfo(){
    return {
        title:title_input.value,
        subject: subject_input.value,
        techstack: techstack_input.value,
        scale: scale_input.value,
        projectStartDate: start_date_input.value,
        projectEndDate: end_date_input.value
    };
}

function getContentInfo(formData){
    var contentDTOS = [];

    content_info.forEach((content, idx)=>{
        var contentDTO={};
        contentDTO["contentTitle"] = content["title"].value;
        contentDTO["content"] = content["content"].value;
        contentDTO["no"] = idx;

        contentDTOS.push(contentDTO);
    });

    return contentDTOS;
}

function setFileToFormData(formData){
    fileList = [];
    content_info.forEach((content,idx)=>{
        var file;

        if(content["file"].value) file = content["file"].files[0];
        else file = new File([" "], "__dummy", {type: "text/plain",});

        formData.append("file", file);
    });
}

function constructFormData(){
    var formData = new FormData();

    var info = getOverviewInfo();
    info["contentDTOS"] = getContentInfo(formData);
    setFileToFormData(formData);

    formData.append('projectDTO', new Blob([JSON.stringify(info)] , {type: "application/json"}));
    formData.append('projectId', getCurrentParams().get("project_id"));

    return formData;
}

function editBtnOnClickHandler(event){
    var data = constructFormData();

    if(checkInput(data)){
        $.ajax({
            url:"/api/project/projects",
            data: data,
            processData: false,
            contentType: false,
            type:"PUT",
            success:function(res){
                const json = JSON.parse(res);
                const res_code = json["code"];
                if(res_code == SUCCESS){
                    alert("프로젝트가 업로드 되었습니다.");
                    const project_id = json["data"]["project_overview"]["project_id"];
                    window.location.href = "/project?project_id=" + project_id;
                }else{
                    alert("프로젝트가 업로드 실패");
                }
            }
        });
    }
}

function changeTo(od){
    while(sub_title_holder.hasChildNodes()){
        sub_title_holder.removeChild(sub_title_holder.firstChild);
    }
    sub_title_holder.appendChild(content_info[od]["title"]);

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

function setButtonText(btn){
    var cur_title = content_info[current_content]["title"];

    if(cur_title.value == ""){
        cur_title.value = "항목" +content_info.length;
        btn.innerText = cur_title.value;
    }
    else
       btn.innerText = summarize(cur_title.value);
}

function constructButton(){
    var btn = createElement("button", "sub_title");
    btn.addEventListener("click", subTitleBtnOnClickHandler);
    setButtonText(btn);
    return btn;
}

function constructNewContentLi(order){
    var content_li = createElement("li", "horizontal");
    content_li.setAttribute("order", order);

    var content_btn = constructButton();
    content_li.appendChild(content_btn);

    return content_li;
}

function setLastOrder(){
    content_last_li.setAttribute("order", parseInt(content_last_li.getAttribute("order")) +1);
}

function createInputAndPushToContentInfo(){
    var ntitle = document.createElement("input");
        ntitle.setAttribute("type", "text");
        ntitle.setAttribute("name", "contentTitle");
        ntitle.addEventListener("input", subTitleInputOnChangeHandler);
    var ncontent = document.createElement("textarea");
        ncontent.setAttribute("name", "content");
        ncontent.setAttribute("cols", 50);
        ncontent.setAttribute("rows", 10);
    var nfile = document.createElement("input");
        nfile.setAttribute("type", "file");
        nfile.setAttribute("name", "file");

    content_info.push({title: ntitle, content: ncontent, file: nfile});
}

function createNewContent(){
    var content_li = constructNewContentLi(content_info.length-1);

    setLastOrder();
    createInputAndPushToContentInfo();

    content_ul.insertBefore(content_li, content_last_li);
    content_last_btn.innerText = "새 항목";
}


function subTitleBtnOnClickHandler(event){
    var li = event.currentTarget.parentElement;
    var od = parseInt(li.getAttribute("order"));

    changeTo(od);
}

function subTitleInputOnChangeHandler(event){
    var t = event.currentTarget.value;
    var btn = content_ul.querySelector(`li:nth-child(${current_content+1}) > button`);
    btn.innerText = summarize(t);
}

function addBtnOnClickHandler(event){
    createNewContent();
    changeTo(content_info.length-1);
}

function checkCurrentUser(userId){
 $.ajax({
        url: "/api/account/current",
        type: "GET",
        success:function(res){
            const json = JSON.parse(res);
            const res_code = json["code"];

            if(res_code==SUCCESS){
                const currentUserId = json["data"]["user_id"];

                if(userId!=currentUserId){
                    alert("해당 게시글의 작성자가 아닙니다.");
                    window.location.href="/project?project_id="+getCurrentParams().get("project_id");
                }
            }else{
                alert("현재 사용자 정보조회에 실패하였습니다.");
            }
        }
    });
}

function setProjectOverviewInputValue(projectOverview){
    title_input.value = projectOverview["main_title"];
    subject_input.value = projectOverview["subject"];
    scale_input.value = projectOverview["scale"];
    techstack_input.value = projectOverview["techstack"];
    start_date_input.value = projectOverview["project_start_date"];
    end_date_input.value = projectOverview["project_end_date"];
}

function setContentValue(projectContent, idx){
    content_info[idx]["title"].value = projectContent["sub_title"];
    content_info[idx]["content"].value = projectContent["content"];

    //    setFile
    //    content_info[idx]["file"].value = projectContent["file_name"];
}

function setContent(projectContentList){
    projectContentList.forEach((projectContent,idx)=>{
        if(idx != 0)
            createNewContent();
        setContentValue(projectContent, idx);
    });
}

function setContentUl(){
    var lis = content_ul.querySelectorAll("li");
    lis.forEach((li, idx)=>{
        var btn = li.querySelector("button");
        btn.innerText = content_info[idx]["title"].value;
    });
}

function setProjectContentList(projectContentList){
    setContent(projectContentList);
    setContentUl();
}

function setInputValue(projectOverview, projectContentList){
    setProjectOverviewInputValue(projectOverview);
    setProjectContentList(projectContentList);
}

function loadProjectAndCheckUser(){
    var params = getCurrentParams();
    var id = params.get("project_id");
    $.ajax({
        url: "/api/project/projects/"+id,
        type: "GET",
        success:function(res){
            const json = JSON.parse(res);
            const res_code = json["code"];

            if(res_code==SUCCESS){
                const projectOverview = json["data"]["project_overview"];
                const projectContentList = json["data"]["project_content_list"];

                checkCurrentUser(projectOverview["user"]["user_id"]);
                setInputValue(projectOverview, projectContentList);
            }else{
                alert("프로젝트 로드에 실패하였습니다.");
            }
        }
    });
}

function init(){
    loadProjectAndCheckUser();
    edit_btn.addEventListener("click", editBtnOnClickHandler);
    add_btn.addEventListener("click", addBtnOnClickHandler);
    content_last_btn.addEventListener("click", subTitleBtnOnClickHandler);
    content_title.addEventListener("input", subTitleInputOnChangeHandler);
}

init();