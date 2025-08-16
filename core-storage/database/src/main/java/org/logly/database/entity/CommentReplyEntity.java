package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "comment_replies")
public class CommentReplyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentReplyId;

    private Long commentId;
    private Long userId;
    private String content;

    protected CommentReplyEntity() {}
}
