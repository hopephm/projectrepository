package com.hope.projectrepository.domain.service.project.comparator.implementation;

import com.hope.projectrepository.domain.entity.ProjectContent;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.service.project.comparator.ProjectContentComparator;

import java.util.Comparator;

public class ProjectContentComparatorImpl implements ProjectContentComparator {
    public Comparator<ProjectContent> basicOrder() {
        return new Comparator<ProjectContent>() {
            @Override
            public int compare(ProjectContent c1, ProjectContent c2) {
                if (c1.getNo() > c2.getNo()) return 1;
                else if (c1.getNo() < c2.getNo()) return -1;
                else return 0;
            }
        };
    }
}
