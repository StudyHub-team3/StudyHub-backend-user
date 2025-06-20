package com.studyhub.backend_user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Slf4j
@Entity
@Table(name = "site_user")
public class SiteUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    @Getter @Setter
    private String email;

    @Column(name = "password", nullable = false)
    @Getter @Setter
    private String password;

    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Column(name = "phone_number", nullable = false)
    @Getter @Setter
    private String phoneNumber;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Getter @Setter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @Getter @Setter
    private LocalDateTime updatedAt;

    @Column(name = "deleted", nullable = false)
    @Getter @Setter
    private Boolean deleted = false;

}
