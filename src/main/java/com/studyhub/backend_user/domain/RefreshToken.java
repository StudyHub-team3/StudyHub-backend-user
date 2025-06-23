package com.studyhub.backend_user.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter @Setter
    private SiteUser user;

    @Column(name = "token", updatable = false, unique = true, nullable = false, length = 1024)
    @Getter @Setter
    private String token;

    @Column(name = "expired_at", nullable = false)
    @Getter @Setter
    private LocalDateTime expiredAt;

    @Column(name = "created_at", nullable = false)
    @Getter @Setter
    private LocalDateTime createdAt;

    @Column(name = "revoked_at")
    @Getter @Setter
    private LocalDateTime revokedAt;

    @Builder
    public RefreshToken(SiteUser user, String token, LocalDateTime expiredAt) {
        this.user = user;
        this.token = token;
        this.expiredAt = expiredAt;
        this.createdAt = LocalDateTime.now();
    }

    public void revoke() {
        this.revokedAt = LocalDateTime.now();
    }

    public boolean isValid() {
        return this.revokedAt == null && LocalDateTime.now().isBefore(this.expiredAt);
    }
}
