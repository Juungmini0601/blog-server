package blog.jungmini.me.database.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = "blog.jungmini.me.database")
@EnableJpaRepositories(basePackages = "blog.jungmini.me.database")
public class JpaConfig {}
