package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;

@Getter
@Table(name = "comment_replies")
public class CommentReplyEntity extends BaseEntity {
    @Id
    private Long commentReplyId;

    private Long commentId;
    private Long userId;
    private String content;
}
