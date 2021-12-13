package com.hope.projectrepository.domain.entity;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "ProjectContent")
@Table(name = "project_content")
public class ProjectContent {
    @Id
    @Column
    @GeneratedValue
    private Long contentId;

    @OneToOne
    @JoinColumn(name = "project_overview", referencedColumnName = "projectId")
    private ProjectOverview projectOverview;

    @Column private Long no;
    @Column private String subTitle;
    @Column private String content;

    @Builder
    public ProjectContent(ProjectOverview projectOverview, String subTitle, String content, Long no){
        this.no = no;
        this.projectOverview = projectOverview;
        this.subTitle = subTitle;
        this.content = content;
    }
}
