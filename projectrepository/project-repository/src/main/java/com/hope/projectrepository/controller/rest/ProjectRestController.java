package com.hope.projectrepository.controller.rest;

import com.hope.projectrepository.compatibility.dto.ProjectContentAndFileInfoDTO;
import com.hope.projectrepository.compatibility.dto.ProjectDTO;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/rest/project")
public class ProjectRestController {
    @Autowired
    ProjectService projectService;

    // @GetMapping("/search")
    @GetMapping("/projects")
    public String getProjects( @RequestParam("category") @Nullable String category,
                                             @RequestParam("orderby") @Nullable String orderby,
                                             @RequestParam("search_text") @Nullable String text) {
        List<ProjectOverview> projectList = projectService.search(category, orderby, text);

        return projectList.toString();
    }

    // @GetMapping("/project/{projectId}")
    @GetMapping("/projects/{projectId}")
    public String getProject (@PathVariable String projectId) {

        ProjectOverview projectOverview = projectService.getProjectOverview(projectId);
        List<ProjectContentAndFileInfoDTO> contentAndFiles = projectService.getProjectContentAndFiles(projectOverview);

        return projectOverview.toString() + contentAndFiles.toString();
    }

    @PostMapping("/projects")
    public String uploadProject(Model model,
                         @RequestPart(value="file") MultipartFile[] files,
                         @RequestPart(value="projectDTO") ProjectDTO projectDTO){
        ProjectOverview newOverview = projectService.uploadProject(projectDTO, files);
        return newOverview.getProjectId().toString();
    }

    @GetMapping("/files/{fileId}")
    public void sendFile(HttpServletResponse response,
                         @PathVariable String fileId){
        projectService.sendFileToClient(response, fileId);
    }
}
