package com.hope.projectrepository.compatibility.dto;

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
    private String startDate;
    private String endDate;
    private ContentDTO[] contentDTOS;
}
