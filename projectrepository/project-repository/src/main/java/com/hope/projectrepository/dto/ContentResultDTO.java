package com.hope.projectrepository.dto;

import com.hope.projectrepository.domain.FileInfo;
import com.hope.projectrepository.domain.ProjectContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentResultDTO {
    private ProjectContent content;
    private FileInfo file;
}
