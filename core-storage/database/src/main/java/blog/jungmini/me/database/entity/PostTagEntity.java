package blog.jungmini.me.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "post_tags")
public class PostTagEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_tag_id")
    private Long postTagId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    // For JPA
    protected PostTagEntity() {}
}
