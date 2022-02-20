package com.hope.projectrepository.domain.service.project.file;

import com.hope.projectrepository.domain.entity.ProjectContent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileManager {
    public ResponseEntity<byte[]> getFile(String fileId);
//    public void sendFileToClient(HttpServletResponse response, String fileId);
    public void uploadFile(ProjectContent content, MultipartFile file);
}
