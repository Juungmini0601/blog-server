package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;

@Getter
@Table(name = "notifications")
public class NotificationEntity extends BaseEntity {
    @Id
    private Long notificationId;

    private Long userId;
    private NotificationType type;
    private String content;
    private Boolean isRead = false;

    public enum NotificationType {
        COMMENT,
        LIKE,
        FOLLOW
    }
}
