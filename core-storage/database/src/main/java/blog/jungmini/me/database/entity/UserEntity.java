package blog.jungmini.me.database.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "introduction", length = 100)
    private String introduction;

    // For JPA
    protected UserEntity() {}

    @Builder
    public UserEntity(
            Long userId,
            String email,
            String nickname,
            String password,
            String profileImageUrl,
            String githubUrl,
            String introduction) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.githubUrl = githubUrl;
        this.introduction = introduction;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
