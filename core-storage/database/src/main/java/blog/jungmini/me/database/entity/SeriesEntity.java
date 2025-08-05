package blog.jungmini.me.database.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "series")
public class SeriesEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "series_id")
    private Long seriesId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // For JPA
    protected SeriesEntity() {}

    @Builder
    public SeriesEntity(Long seriesId, Long userId, String name) {
        this.seriesId = seriesId;
        this.userId = userId;
        this.name = name;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public void setName(String name) {
        this.name = name;
    }
}
