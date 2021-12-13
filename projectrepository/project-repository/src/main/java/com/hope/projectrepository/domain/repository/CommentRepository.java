package com.hope.projectrepository.domain.repository;

import com.hope.projectrepository.domain.entity.Comment;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findByUser(User user);
    public List<Comment> findByProjectOverview(ProjectOverview projectOverview);

}
