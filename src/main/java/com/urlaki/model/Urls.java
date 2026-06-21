package com.urlaki.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_owner_bigurl",
                columnNames = {"user_id", "big_u_r_l"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Urls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String bigURL;

    @Column(unique = true, nullable = false)
    private String shortURL;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}