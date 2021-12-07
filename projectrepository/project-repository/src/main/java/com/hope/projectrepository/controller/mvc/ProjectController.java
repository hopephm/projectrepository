package com.hope.projectrepository.controller.mvc;

import com.hope.projectrepository.domain.FileInfo;
import com.hope.projectrepository.domain.ProjectContent;
import com.hope.projectrepository.domain.ProjectOverview;
import com.hope.projectrepository.dto.ContentDTO;
import com.hope.projectrepository.dto.ContentResultDTO;
import com.hope.projectrepository.dto.ProjectDTO;
import com.hope.projectrepository.repository.FileInfoRepository;
import com.hope.projectrepository.repository.ProjectContentRepository;
import com.hope.projectrepository.repository.ProjectOverviewRepository;
import com.hope.projectrepository.service.FileService;
import com.hope.projectrepository.service.ProjectService;
import com.hope.projectrepository.util.Result;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    FileService fileService;

    @Autowired
    ProjectOverviewRepository projectOverviewRepository;


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

    @PostMapping("/upload")
    public @ResponseBody String uploadProject(Model model,
                                                @RequestPart(value="file") MultipartFile[] files,
                                                @RequestPart(value="projectDTO") ProjectDTO projectDTO){
        ProjectOverview newOverview = projectService.uploadProject(projectDTO, files);
        return newOverview.getProjectId().toString();
    }


    @GetMapping("/download/{fileId}")
    public void sendFile(HttpServletResponse response,
                             @PathVariable String fileId){
        // 파일 업로드 실패 핸들링
        fileService.sendFileToClient(response, fileId);
    }
}