package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "oauths")
public class OAuthEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oauthId;

    private Long userId;
    private String provider;
    private String providerId;

    // For JPA
    protected OAuthEntity() {}
}
