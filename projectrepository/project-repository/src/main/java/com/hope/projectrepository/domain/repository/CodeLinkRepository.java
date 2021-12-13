package com.hope.projectrepository.domain.repository;

import com.hope.projectrepository.domain.entity.CodeLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeLinkRepository extends JpaRepository<CodeLink, Long> {
}
