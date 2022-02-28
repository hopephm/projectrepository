package com.hope.projectrepository.domain.service.project;

import com.hope.projectrepository.util.dto.ProjectContentAndFileInfoDTO;
import com.hope.projectrepository.util.dto.ProjectDTO;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    public List<ProjectOverview> search(String category, String orderby, String text);
    public ProjectOverview getProjectOverview(String projectId);
    public List<ProjectContentAndFileInfoDTO> getProjectContentAndFiles(ProjectOverview projectOverview);
    public ProjectOverview uploadProject(ProjectDTO projectDTO, MultipartFile[] files);
    public ProjectOverview updateProject(ProjectDTO projectDTO, MultipartFile[] files, String projectId);

    public ResponseEntity<byte[]> getFile(String fileId);
//    public void sendFileToClient(HttpServletResponse response, String fileId);
}
