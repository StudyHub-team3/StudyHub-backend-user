package com.studyhub.backend_user.secret.jwt.domain.repository;

import com.studyhub.backend_user.secret.jwt.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
