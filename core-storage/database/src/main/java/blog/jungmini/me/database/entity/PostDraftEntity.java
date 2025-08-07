package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;

@Getter
@Table(name = "post_drafts")
public class PostDraftEntity extends BaseEntity {
    @Id
    private Long postDraftId;

    private Long userId;
    private String title;
    private String content;

    // For JPA
    protected PostDraftEntity() {}
}
