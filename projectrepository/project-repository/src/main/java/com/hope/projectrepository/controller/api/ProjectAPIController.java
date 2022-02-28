package com.hope.projectrepository.controller.api;

import com.hope.projectrepository.util.dto.ProjectContentAndFileInfoDTO;
import com.hope.projectrepository.util.dto.ProjectDTO;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.service.project.ProjectService;
import com.hope.projectrepository.exception.handle.ExceptionHandling;
import com.hope.projectrepository.util.response.json.JsonResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

// 여유될 때, Restful하게 CRUD 맞춰서 추가하자
@RestController
@RequestMapping("/api/project")
public class ProjectAPIController {
    @Autowired
    ProjectService projectService;

    @GetMapping("/projects/{projectId}")
    @ExceptionHandling
    public String getProject (@PathVariable String projectId) {

        ProjectOverview projectOverview = projectService.getProjectOverview(projectId);
        List<ProjectContentAndFileInfoDTO> contentAndFiles = projectService.getProjectContentAndFiles(projectOverview);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("project_overview", projectOverview);
        jr.addData("project_content_list", contentAndFiles);

        return jr.getResponse();
    }

    @GetMapping("/projects")
    @ExceptionHandling
    public String getProjectsByFiltering( @RequestParam("category") @Nullable String category,
                               @RequestParam("orderby") @Nullable String orderby,
                               @RequestParam("search_text") @Nullable String text) {
        List<ProjectOverview> projectList = projectService.search(category, orderby, text);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("project_list", projectList);

        return jr.getResponse();
    }

    @PostMapping("/projects")
    @ExceptionHandling
    public String createProject(@RequestPart(value="file") MultipartFile[] files,
                                @RequestPart(value="projectDTO") ProjectDTO projectDTO){
        ProjectOverview newOverview = projectService.uploadProject(projectDTO, files);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("project_overview", newOverview);

        return jr.getResponse();
    }

    @PutMapping("/projects")
    @ExceptionHandling
    public String updateProject(@RequestPart(value="file") MultipartFile[] files,
                                @RequestPart(value="projectDTO") ProjectDTO projectDTO,
                                @RequestPart(value="projectId") String projectId){
        ProjectOverview projectOverview = projectService.updateProject(projectDTO, files, projectId);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("project_overview", projectOverview);

        return jr.getResponse();
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileId){
        ResponseEntity<byte[]> entity = projectService.getFile(fileId);
        return entity;
    }
}
