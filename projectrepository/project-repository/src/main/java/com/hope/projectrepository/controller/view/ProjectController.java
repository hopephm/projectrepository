package com.hope.projectrepository.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


// 예외 핸들링
@Controller
public class ProjectController {
    @GetMapping("/search")
    public String projectHome(){
        return "project/project_search";
    }

    @GetMapping("/project")
    public String projectLookup(@RequestParam("project_id") String projectId){
        return "project/project_lookup";
    }

    @GetMapping("/upload")
    public String uploadProjectView(Model model){
        return "project/project_upload";
    }
}
