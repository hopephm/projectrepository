package com.hope.projectrepository.repository;

import com.hope.projectrepository.domain.ProjectContent;
import com.hope.projectrepository.domain.ProjectOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectContentRepository extends JpaRepository<ProjectContent, Long> {
    @Query("SELECT pc FROM ProjectContent pc WHERE pc.content LIKE %:str% OR pc.subTitle LIKE %:str%")
    public List<ProjectContent> findByIncludeContent(@Param("str") String str);
    public List<ProjectContent> findByProjectOverview(ProjectOverview projectOverview);
}
