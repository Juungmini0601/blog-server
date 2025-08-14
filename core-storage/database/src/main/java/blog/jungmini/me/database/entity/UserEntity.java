package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString(exclude = "password")
@Getter
@Table("users")
public class UserEntity extends BaseEntity {
    @Id
    private Long userId;

    private String email;
    private String nickname;
    private String password;
    private String profileImageUrl;
    private String githubUrl;
    private String introduction;

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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
