package com.hope.projectrepository.util.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private String title;
    private String subject;
    private String techstack;
    private String scale;
    private String projectStartDate;
    private String projectEndDate;
    private ProjectContentDTO[] contentDTOS;
}
