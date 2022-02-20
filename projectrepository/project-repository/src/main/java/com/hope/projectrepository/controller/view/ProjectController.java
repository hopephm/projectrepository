package com.hope.projectrepository.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


// 예외 핸들링
@Controller
public class ProjectController {
    @GetMapping("/search")
    public String projectSearchPage(){
        return "project/project_search";
    }

    @GetMapping("/project")
    public String projectLookupPage(@RequestParam("project_id") String projectId){
        return "project/project_lookup";
    }

    @GetMapping("/upload")
    public String projectUploadPage(){
        return "project/project_upload";
    }

    @GetMapping("/upload/edit")
    public String projectUpdatePage(@RequestParam("project_id") String projectId){ return "project/project_edit";}
}
