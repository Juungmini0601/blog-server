package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "series")
public class SeriesEntity extends BaseEntity {
    @Id
    private Long seriesId;

    private Long userId;
    private String name;

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
