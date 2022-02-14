package com.hope.projectrepository.domain.service.project;

import com.hope.projectrepository.compatibility.dto.ProjectContentAndFileInfoDTO;
import com.hope.projectrepository.compatibility.dto.ProjectDTO;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProjectService {
    public List<ProjectOverview> search(String category, String orderby, String text);
    public ProjectOverview getProjectOverview(String projectId);
    public List<ProjectContentAndFileInfoDTO> getProjectContentAndFiles(ProjectOverview projectOverview);
    public ProjectOverview uploadProject(ProjectDTO projectDTO, MultipartFile[] files);
    public void sendFileToClient(HttpServletResponse response, String fileId);

}
