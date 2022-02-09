package com.hope.projectrepository.domain.service.project.implementation;

import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.compatibility.dto.ProjectContentAndFileInfoDTO;
import com.hope.projectrepository.compatibility.dto.ProjectDTO;
import com.hope.projectrepository.domain.service.project.ProjectService;
import com.hope.projectrepository.domain.service.project.file.FileManager;
import com.hope.projectrepository.domain.service.project.finder.ProjectFinder;
import com.hope.projectrepository.domain.service.project.uploader.ProjectUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectFinder projectFinder;
    @Autowired
    ProjectUploader projectUploader;
    @Autowired
    FileManager fileManager;

    public List<ProjectOverview> search(String category, String orderby, String text){
        return projectFinder.search(category,orderby, text);
    }

    public ProjectOverview getProjectOverview(String projectId){
        return projectFinder.getProjectOverview(projectId);
    }

    public List<ProjectContentAndFileInfoDTO> getProjectContentAndFiles(ProjectOverview projectOverview){
        return projectFinder.getProjectContentAndFiles(projectOverview);
    }

    public ProjectOverview uploadProject(ProjectDTO projectDTO, MultipartFile[] files){
        return projectUploader.uploadProject(projectDTO, files);
    }

    public void sendFileToClient(HttpServletResponse response, String fileId){
        fileManager.sendFileToClient(response, fileId);
    }


}
