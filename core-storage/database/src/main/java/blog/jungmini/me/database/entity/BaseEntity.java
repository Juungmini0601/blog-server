package blog.jungmini.me.database.entity;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public abstract class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
