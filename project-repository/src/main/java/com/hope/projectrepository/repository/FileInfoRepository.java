package com.hope.projectrepository.repository;

import com.hope.projectrepository.domain.FileInfo;
import com.hope.projectrepository.domain.ProjectContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    public FileInfo findByProjectContent(ProjectContent projectContent);
}
