package com.hope.projectrepository.repository;

import com.hope.projectrepository.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUserId(Long userId);
    public User findByLoginId(String loginId);
    public User findByNickname(String nickname);
    public List<User> findByEmail(String email);
}
