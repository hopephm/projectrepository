package com.hope.projectrepository.domain.repository;

import com.hope.projectrepository.domain.entity.FileInfo;
import com.hope.projectrepository.domain.entity.ProjectContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    public FileInfo findByProjectContent(ProjectContent projectContent);
}
