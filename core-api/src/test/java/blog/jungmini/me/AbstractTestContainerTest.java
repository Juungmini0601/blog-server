package blog.jungmini.me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;

@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTestContainerTest {
    @LocalServerPort
    protected int port;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected ObjectMapper objectMapper = new ObjectMapper();

    static final GenericContainer<?> redis;
    static final MySQLContainer<?> mysql;
    static final String REDIS_PASSWORD = "qwer1234";

    static {
        mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.40"))
                .withDatabaseName("my-chat")
                .withUsername("root")
                .withPassword("qwer1234")
                .withReuse(true);
        mysql.start();

        redis = new GenericContainer<>(DockerImageName.parse("redis:7.4.2"))
                .withCommand("redis-server", "--requirepass", REDIS_PASSWORD)
                .withExposedPorts(6379)
                .withReuse(true);
        redis.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        // MySQL container properties
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);

        // Redis container properties
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("spring.data.redis.password", () -> REDIS_PASSWORD);
    }
}
