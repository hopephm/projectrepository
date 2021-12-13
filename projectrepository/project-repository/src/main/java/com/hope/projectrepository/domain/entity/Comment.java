package com.hope.projectrepository.domain.entity;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "Comment")
@Table(name = "comment")
public class Comment {
    @Id
    @Column
    @GeneratedValue
    private Long commentId;

    @OneToOne
    @JoinColumn(name = "project_overview", referencedColumnName = "projectId")
    private ProjectOverview projectOverview;

    @OneToOne
    @JoinColumn(name = "user", referencedColumnName = "userId")
    private User user;

    @Column String comment;

    @Builder
    public Comment(ProjectOverview projectOverview, User user, String comment){
        this.projectOverview = projectOverview;
        this.user = user;
        this.comment = comment;
    }
}
