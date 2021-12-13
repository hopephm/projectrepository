package com.hope.projectrepository.controller.mvc;

import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.compatibility.dto.ContentResultDTO;
import com.hope.projectrepository.compatibility.dto.ProjectDTO;
import com.hope.projectrepository.domain.repository.ProjectOverviewRepository;
import com.hope.projectrepository.domain.service.file.FileService;
import com.hope.projectrepository.domain.service.project.ProjectService;
import com.hope.projectrepository.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    FileService fileService;

    @Autowired
    ProjectOverviewRepository projectOverviewRepository;


    ///*                *///
    //         Get        //
    ///*                *///
    @GetMapping("/search")
    public String projectHome(Model model,
                              @RequestParam("category") @Nullable String category,
                              @RequestParam("orderby") @Nullable String orderby,
                              @RequestParam("search_text") @Nullable String text){

        List<ProjectOverview> projectList = null;

        if(category != null && text != null && orderby != null)
            projectList = projectService.search(category, orderby, text);

        model.addAttribute("projectList", projectList);

        return "project/project_search";
    }

    @GetMapping("/project/{projectId}")
    public String projectLookup(Model model,
                                @PathVariable String projectId){
        ProjectOverview projectOverview = projectOverviewRepository.getById(Long.parseLong(projectId));

        // 리스트 뿌리게 변경
        List<ContentResultDTO> contentsInfo = projectService.getContentsInfo(projectOverview);
//        List<FileInfo> filesInfo = projectService.getFilesInfo(contentsInfo);

        model.addAttribute("project_overview", projectOverview);
        model.addAttribute("contents", contentsInfo);

        return "project/project_lookup";
    }


    @GetMapping("/upload")
    public String uploadProjectView(Model model){
        return "project/project_upload";
    }

    @GetMapping("/download/{fileId}")
    public void sendFile(HttpServletResponse response,
                             @PathVariable String fileId){
        // 파일 업로드 실패 핸들링
        fileService.sendFileToClient(response, fileId);
    }

    ///*                *///
    //        Post        //
    ///*                *///
    @PostMapping("/upload")
    public @ResponseBody String uploadProject(Model model,
                                              @RequestPart(value="file") MultipartFile[] files,
                                              @RequestPart(value="projectDTO") ProjectDTO projectDTO){
        ProjectOverview newOverview = projectService.uploadProject(projectDTO, files);
        return Result.SUCCESS;
    }
}
