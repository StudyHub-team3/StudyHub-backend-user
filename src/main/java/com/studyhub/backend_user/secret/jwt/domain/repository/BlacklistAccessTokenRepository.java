package com.studyhub.backend_user.secret.jwt.domain.repository;

import com.studyhub.backend_user.secret.jwt.domain.BlacklistAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistAccessTokenRepository extends CrudRepository<BlacklistAccessToken, String> {
}
