package com.hope.projectrepository.repository;

import com.hope.projectrepository.domain.ProjectOverview;
import com.hope.projectrepository.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectOverviewRepository extends JpaRepository<ProjectOverview, Long> {
    public List<ProjectOverview> findByUser(User user);

    @Query("SELECT po FROM ProjectOverview po WHERE po.mainTitle LIKE %:str%")
    public List<ProjectOverview> findByIncludeTitle(@Param("str") String str);

    @Query("SELECT po FROM ProjectOverview po WHERE po.techStack LIKE %:str%")
    public List<ProjectOverview> findByIncludeTechstack(@Param("str") String str);

    @Query("SELECT po FROM ProjectOverview po WHERE EXISTS (select u from User u where po.user = u and u.nickname like %:str%)")
    public List<ProjectOverview> findByIncludeNickname(@Param("str") String str);
}
