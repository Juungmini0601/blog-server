package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "series")
public class SeriesEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seriesId;

    private Long userId;
    private String name;

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
