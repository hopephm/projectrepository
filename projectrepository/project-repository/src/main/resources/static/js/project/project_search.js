const search = document.querySelector(".search"),
    category = search.querySelector("select[name=category]"),
    orderby = search.querySelector("select[name=orderby]")
    search_text = search.querySelector("input[name=search_text]"),
    search_btn = search.querySelector(".search_btn");

const project_list = document.querySelector(".project_list");

function deletePreviousResult(){
    if(project_list.hasChildNodes()){
        project_list.childNodes.forEach((element)=>{
            element.remove();
        });
    }
}

function createHeadTr(){
    var tr = createElement("tr");
    var title = createElement("td", "__disc", "제목");
    var subject = createElement("td", "__disc", "주제");
    var scale = createElement("td", "__disc", "규모");
    var techstack = createElement("td", "__disc", "기술스택");
    var project_start_date = createElement("td", "__disc", "시작날짜");
    var project_end_date = createElement("td", "__disc", "종료날짜");
    var user = createElement("td", "__disc", "작성자");

    tr.appendChild(title);
    tr.appendChild(subject);
    tr.appendChild(scale);
    tr.appendChild(techstack);
    tr.appendChild(project_start_date);
    tr.appendChild(project_end_date);
    tr.appendChild(user);

    return tr;
}

function createProjectOverviewTr(project){
    var tr = createElement("tr");
    var title = createElement("td", "__square", project["main_title"]);
    var subject = createElement("td", "__square", project["subject"]);
    var scale = createElement("td", "__square", project["scale"]);
    var techstack = createElement("td", "__square", project["techstack"]);
    var project_start_date = createElement("td", "__square", project["project_start_date"]);
    var project_end_date = createElement("td", "__square", project["project_end_date"]);
    var user = createElement("td", "__square", project["user"]["nickname"]);
    var lookup = createElement("td", "lookup");
    var link = createElement("a", "none", "조회");
    link.setAttribute("href", "/project?project_id="+project["project_id"]);
    lookup.appendChild(link);

    tr.appendChild(title);
    tr.appendChild(subject);
    tr.appendChild(scale);
    tr.appendChild(techstack);
    tr.appendChild(project_start_date);
    tr.appendChild(project_end_date);
    tr.appendChild(user);
    tr.appendChild(lookup);

    return tr;
}

function showProjectList(projectList){
    var table = createElement("table");
    var tbody = createElement("tbody");
    var headTr = createHeadTr();
    tbody.appendChild(headTr);

    projectList.forEach((project)=>{
        var projectTr = createProjectOverviewTr(project);
        tbody.appendChild(projectTr);
    });

    table.appendChild(tbody);
    project_list.appendChild(table);
}

function searchBtnOnClickHandler(event){
    var category_val = category.value;
    var orderby_val = orderby.value;
    var search_text_val = search_text.value;

    var data = {
        category: category_val,
        orderby: orderby_val,
        search_text: search_text_val
    };

    $.ajax({
        url: "/rest/project/projects",
        data: data,
        type: "GET",
        success:function(res){
            const json = JSON.parse(res);
            const res_code = json["code"];

            if(res_code==SUCCESS){
                deletePreviousResult();
                showProjectList(json["data"]["project_list"]);
            }else{
                alert("프로젝트 로드에 실패하였습니다.");
            }
        }
    });
}

function init(){
    search_btn.addEventListener("click", searchBtnOnClickHandler);
}

window.onload=init();