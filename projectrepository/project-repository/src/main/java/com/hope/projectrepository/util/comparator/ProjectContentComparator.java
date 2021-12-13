package com.hope.projectrepository.util.comparator;

import com.hope.projectrepository.domain.entity.ProjectContent;

import java.util.Comparator;

public class ProjectContentComparator implements Comparator<ProjectContent> {
    @Override
    public int compare (ProjectContent c1, ProjectContent c2){
        if(c1.getNo()>c2.getNo()) return 1;
        else if(c1.getNo() < c2.getNo()) return -1;
        else return 0;
    }
}
