const overview = document.querySelector(".overview"),
    main_title = overview.querySelector("#main_title"),
    subject = overview.querySelector("#subject"),
    techstack = overview.querySelector("#techstack"),
    scale = overview.querySelector("#scale"),
    project_start_date = overview.querySelector("#project_start_date"),
    project_end_date = overview.querySelector("#project_end_date")
    ;

const content_list = document.querySelector(".content_list");
const content_holder = document.querySelector(".content_holder");

var current_content = 0;
var content = [];

function getCurrentParams(){
    const url = new URL(window.location.href);
    return url.searchParams;
}

function showProjectOverview(projectOverview){
    main_title.innerText=projectOverview["main_title"];
    subject.innerText=projectOverview["subject"];
    techstack.innerText=projectOverview["techstack"];
    scale.innerText=projectOverview["scale"];
    project_start_date.innerText=projectOverview["project_start_date"];
    project_end_date.innerText=projectOverview["project_end_date"];
}

function subtitleBtnOnClickHandler(event){
    var li = event.currentTarget.parentElement;
    var idx = $(li).index();

    content[current_content].setAttribute("style", "display:none");
    content[idx].setAttribute("style", "display:block");
    current_content = idx;
}

function constructContentList(subTitle){
    var li = createElement("li", "last_content horizontal");
    var button = createElement("button", "sub_title", subTitle);

    li.appendChild(button);
    button.addEventListener("click",subtitleBtnOnClickHandler);
    content_list.appendChild(li);
}

function createSubTitleTr(subTitle){
    var tr = createElement("tr");
    var th = createElement("th", "_disc", "항목이름");
    var td = createElement("td", "__square", subTitle);
    tr.appendChild(th);
    tr.appendChild(td);
    return tr;
}

function createFileInfoTr(fileId, fileName){
    var tr = createElement("tr");
    var th = createElement("th", "_disc", "첨부파일");
    var td = createElement("td", "__square");

    if(fileId != "null"){
        var a = createElement("a", "none", fileName);
        a.setAttribute("href", "/rest/project/files/"+fileId);
        td.appendChild(a);
    }

    tr.appendChild(th);
    tr.appendChild(td);
    return tr;
}

function createContentHeadTr(){
    var tr = createElement("tr");
    var th = createElement("th", "content_disc", "내용");
    th.setAttribute("colspan", "2");
    tr.appendChild(th);
    return tr;
}

function newLineProcessing(text){
    return text.replace(/(<br>|<br\/>|<br \/>|<\/br>)/g, '\r\n');
}

function createContentTr(text){
    var tr = createElement("tr");
    var th = createElement("th", "content_square", newLineProcessing(text));
    th.setAttribute("colspan", "2");
    tr.appendChild(th);
    return tr;
}

function constructContent(projectContent){
    var table = createElement("table", "content");
    var tbody = createElement("tbody");
    var subTitleTr = createSubTitleTr(projectContent["sub_title"]);
    var fileInfoTr = createFileInfoTr(projectContent["file_id"], projectContent["file_name"]);
    var contentHeadTr = createContentHeadTr();
    var contentTr = createContentTr(projectContent["content"]);
    tbody.appendChild(subTitleTr);
    tbody.appendChild(fileInfoTr);
    tbody.appendChild(contentHeadTr);
    tbody.appendChild(contentTr);
    table.appendChild(tbody);
    table.style.display="none";
    content_holder.appendChild(table);
    content.push(table);
}

function constructProjectContent(projectContentList){
    projectContentList.forEach((content)=>{
        constructContentList(content["sub_title"]);
        constructContent(content);
    });
}

function showContent(){
    content[current_content].style.display="block";
}

function loadProject(){
    var params = getCurrentParams();
    var id = params.get("project_id");

    $.ajax({
        url: "/rest/project/projects/"+id,
        type: "GET",
        success:function(res){
            const json = JSON.parse(res);
            const res_code = json["code"];

            if(res_code==SUCCESS){
                const projectOverview = json["data"]["project_overview"];
                showProjectOverview(projectOverview);

                const projectContentList = json["data"]["project_content_list"];
                constructProjectContent(projectContentList);

                showContent();
            }else{
                alert("프로젝트 로드에 실패하였습니다.");
            }
        }
    });
}

function init(){
    loadProject();
}

init();