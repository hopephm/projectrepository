package com.hope.projectrepository.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


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
