package com.hope.projectrepository.repository;

import com.hope.projectrepository.domain.Comment;
import com.hope.projectrepository.domain.ProjectOverview;
import com.hope.projectrepository.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findByUser(User user);
    public List<Comment> findByProjectOverview(ProjectOverview projectOverview);

}
