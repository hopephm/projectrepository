package com.hope.projectrepository.util.comparator;

import com.hope.projectrepository.domain.entity.ProjectOverview;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ProjectOverviewComparator {
    public Comparator<ProjectOverview> orderByStartDate(){
        return new Comparator<ProjectOverview>(){
            @Override
            public int compare(ProjectOverview o1, ProjectOverview o2){
                if(o1.getProjectStartDate().isAfter(o2.getProjectStartDate())) return 1;
                else if(o1.getProjectStartDate().isBefore(o2.getProjectStartDate())) return -1;
                else return 0;
            }
        };
    }

    public Comparator<ProjectOverview> orderByEndDate(){
        return new Comparator<ProjectOverview>(){
            @Override
            public int compare(ProjectOverview o1, ProjectOverview o2){
                if(o1.getProjectEndDate().isAfter(o2.getProjectEndDate())) return 1;
                else if(o1.getProjectEndDate().isBefore(o2.getProjectEndDate())) return -1;
                else return 0;
            }
        };
    }

    public Comparator<ProjectOverview> orderByNickname(){
        return new Comparator<ProjectOverview>(){
            @Override
            public int compare(ProjectOverview o1, ProjectOverview o2){
                if(o1.getUser().getNickname().compareTo(o2.getUser().getNickname()) > 0) return 1;
                else if(o1.getUser().getNickname().compareTo(o2.getUser().getNickname()) < 0) return -1;
                else return 0;
            }
        };
    }

    public Comparator<ProjectOverview> orderByTitle(){
        return new Comparator<ProjectOverview>(){
            @Override
            public int compare(ProjectOverview o1, ProjectOverview o2){
                if(o1.getMainTitle().compareTo(o2.getMainTitle()) > 0) return 1;
                else if(o1.getMainTitle().compareTo(o2.getMainTitle()) < 0) return -1;
                else return 0;
            }
        };
    }
}