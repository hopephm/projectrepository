package com.hope.projectrepository.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "FileInfo")
@Table(name = "file_info")
public class FileInfo {
    @Id
    @Column
    @GeneratedValue
    private Long fileId;

    @OneToOne
    @JoinColumn(name = "project_content", referencedColumnName = "contentId")
    private ProjectContent projectContent;

    @Column private String fileName;
    @Column private String filePath;

    @Builder
    public FileInfo(ProjectContent projectContent, String filePath, String fileName){
        this.projectContent = projectContent;
        this.filePath = filePath;
        this.fileName = fileName;
    }
}
