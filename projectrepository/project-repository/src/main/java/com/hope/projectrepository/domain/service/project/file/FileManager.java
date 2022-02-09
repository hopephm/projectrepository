package com.hope.projectrepository.domain.service.project.file;

import com.hope.projectrepository.domain.entity.ProjectContent;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileManager {
    public void sendFileToClient(HttpServletResponse response, String fileId);
    public void uploadFile(ProjectContent content, MultipartFile file);
}
