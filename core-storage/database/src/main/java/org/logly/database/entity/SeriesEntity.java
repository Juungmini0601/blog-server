package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.*;

@ToString
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
@Table(name = "series")
public class SeriesEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seriesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String name;

    private Long postCount;

    @PrePersist
    void prePersist() {
        if (this.postCount == null) {
            this.postCount = 0L;
        }
    }
}
