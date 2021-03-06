package com.hope.projectrepository.domain.service.project.uploader;

import com.hope.projectrepository.util.dto.ProjectDTO;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectUploader {
    public ProjectOverview uploadProject(ProjectDTO projectDTO, MultipartFile[] files);
    public ProjectOverview updateProject(ProjectDTO projectDTO, MultipartFile[] files, String projectId);
}
