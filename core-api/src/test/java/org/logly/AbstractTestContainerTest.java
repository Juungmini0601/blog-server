package org.logly;

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
import org.testcontainers.containers.PostgreSQLContainer;
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
    static final PostgreSQLContainer<?> postgresql;
    static final String REDIS_PASSWORD = "qwer1234";

    static {
        postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.4"))
                .withDatabaseName("postgres")
                .withUsername("test_user")
                .withPassword("qwer1234")
                .withReuse(true);
        postgresql.start();

        redis = new GenericContainer<>(DockerImageName.parse("redis:7.4.2"))
                .withCommand("redis-server", "--requirepass", REDIS_PASSWORD)
                .withExposedPorts(6379)
                .withReuse(true);
        redis.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL container properties
        registry.add("spring.datasource.url", () -> postgresql.getJdbcUrl() + "?currentSchema=logly");
        registry.add("spring.datasource.username", postgresql::getUsername);
        registry.add("spring.datasource.password", postgresql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        // Redis container properties
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("spring.data.redis.password", () -> REDIS_PASSWORD);
    }
}
