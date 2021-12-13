package com.hope.projectrepository.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "CodeLink")
@Table(name="code_link")
public class CodeLink {
    @Id
    @Column
    @GeneratedValue
    private Long linkId;

    @OneToOne
    @JoinColumn(name = "project_content", referencedColumnName = "contentId")
    private ProjectContent projectContent;

    @Column private String linkName;
    @Column private String codeLinkUrl;

    @Builder
    public CodeLink(ProjectContent projectContent, String linkName, String codeLinkUrl){
        this.projectContent = projectContent;
        this.linkName = linkName;
        this.codeLinkUrl = codeLinkUrl;
    }
}
