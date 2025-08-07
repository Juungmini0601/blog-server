package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;

@Getter
@Table(name = "tags")
public class TagEntity extends BaseEntity {
    @Id
    private Long tagId;

    private String name;
}
