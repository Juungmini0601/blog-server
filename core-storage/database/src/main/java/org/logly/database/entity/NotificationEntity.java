package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "notifications")
public class NotificationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String content;
    private Boolean isRead = false;

    protected NotificationEntity() {}

    public enum NotificationType {
        COMMENT,
        LIKE,
        FOLLOW
    }
}
