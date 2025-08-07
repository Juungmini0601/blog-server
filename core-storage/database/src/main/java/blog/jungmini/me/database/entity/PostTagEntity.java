package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;

@Getter
@Table(name = "post_tags")
public class PostTagEntity extends BaseEntity {
    @Id
    private Long postTagId;

    private Long postId;
    private Long tagId;
}
