package blog.jungmini.me.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "tags")
public class TagEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    // For JPA
    protected TagEntity() {}
}
