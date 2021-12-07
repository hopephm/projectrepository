package com.hope.projectrepository.repository;

import com.hope.projectrepository.domain.CodeLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeLinkRepository extends JpaRepository<CodeLink, Long> {
}
