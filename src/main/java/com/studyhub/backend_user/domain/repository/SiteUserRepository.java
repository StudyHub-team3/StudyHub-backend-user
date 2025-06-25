package com.studyhub.backend_user.domain.repository;

import com.studyhub.backend_user.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    SiteUser findByEmail(String email);
}
