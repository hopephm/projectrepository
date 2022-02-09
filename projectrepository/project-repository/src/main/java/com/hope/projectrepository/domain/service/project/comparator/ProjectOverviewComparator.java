package com.hope.projectrepository.domain.service.project.comparator;

import com.hope.projectrepository.domain.entity.ProjectOverview;

import java.util.Comparator;

public interface ProjectOverviewComparator {
    public Comparator<ProjectOverview> orderByStartDate();
    public Comparator<ProjectOverview> orderByEndDate();
    public Comparator<ProjectOverview> orderByNickname();
    public Comparator<ProjectOverview> orderByTitle();
}
