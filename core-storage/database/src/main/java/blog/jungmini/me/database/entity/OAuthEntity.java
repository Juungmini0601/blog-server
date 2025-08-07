package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;

@Getter
@Table(name = "oauths")
public class OAuthEntity extends BaseEntity {
    @Id
    private Long oauthId;

    private Long userId;
    private String provider;
    private String providerId;

    // For JPA
    protected OAuthEntity() {}
}
