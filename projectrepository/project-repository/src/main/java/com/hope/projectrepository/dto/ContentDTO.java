package com.hope.projectrepository.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ContentDTO {
    private String contentTitle;
    private String content;
    private Long no;
}
