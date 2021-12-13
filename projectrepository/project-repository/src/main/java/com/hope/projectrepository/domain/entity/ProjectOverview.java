package com.hope.projectrepository.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "ProjectOverview")
@Table(name = "project_overview")
public class ProjectOverview {
    @Id
    @Column
    @GeneratedValue
    private Long projectId;

    @OneToOne
    @JoinColumn(name="user", referencedColumnName = "userId")
    private User user;

    @Column private String mainTitle;
    @Column private String subject;
    @Column private String scale;
    @Column private String techStack;
    @Column private LocalDate projectStartDate;
    @Column private LocalDate projectEndDate;
    @Column private LocalDateTime createDate;
    @Column private LocalDateTime updateDate;

    @Builder
    public ProjectOverview(User user, String mainTitle, String subject, String scale, String techStack, LocalDate projectStartDate, LocalDate projectEndDate, LocalDateTime createDate, LocalDateTime updateDate){
        this.user = user;
        this.mainTitle = mainTitle;
        this.subject = subject;
        this.scale = scale;
        this.techStack = techStack;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

}
