package com.hope.projectrepository.domain.service.project.comparator;

import com.hope.projectrepository.domain.entity.ProjectContent;

import java.util.Comparator;

public interface ProjectContentComparator {
    public Comparator<ProjectContent> basicOrder();
}
