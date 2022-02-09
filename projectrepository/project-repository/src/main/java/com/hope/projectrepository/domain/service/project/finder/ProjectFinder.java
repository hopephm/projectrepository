package com.hope.projectrepository.domain.service.project.finder;

import com.hope.projectrepository.compatibility.dto.ProjectContentAndFileInfoDTO;
import com.hope.projectrepository.domain.entity.ProjectOverview;

import java.util.List;

public interface ProjectFinder {
    public ProjectOverview getProjectOverview(String projectId);
    public List<ProjectContentAndFileInfoDTO> getProjectContentAndFiles(ProjectOverview projectOverview);
    public List<ProjectOverview> search(String category, String orderby, String text);
}
