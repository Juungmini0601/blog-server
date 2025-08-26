package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;
    private String content;
    private String thumbnailUrl;
    private Boolean isPublic = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = true)
    private SeriesEntity series;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private PostStatisticsEntity statistics;

    public boolean isAuthor(UserEntity userEntity) {
        return this.user.equals(userEntity);
    }
}
