package org.logly.batch.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

import org.logly.database.entity.UserEntity;

@Slf4j
@Configuration
public class TestUserDataGenerationBatchConfig {
    private static final int CHUNK_SIZE = 1000;
    private static final int USER_COUNT = 200_000;

    @Bean
    public Job userDataGenerationJob(JobRepository jobRepository, Step createUsersStep) {
        return new JobBuilder("userDataGenerationJob", jobRepository)
                .start(createUsersStep)
                .build();
    }

    @Bean
    @StepScope
    public Step createUsersStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<UserEntity> userReader,
            ItemWriter<UserEntity> userWriter) {
        return new StepBuilder("createUsersStep", jobRepository)
                .<UserEntity, UserEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(userReader)
                .writer(userWriter)
                .listener(userChunkListener())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<UserEntity> userReader() {
        List<UserEntity> users = generateUsers();
        return new ListItemReader<>(users);
    }

    @Bean
    @StepScope
    public ItemWriter<UserEntity> userWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<UserEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    @StepScope
    public ChunkListener userChunkListener() {
        return new ChunkListener() {
            private final AtomicLong processedCount = new AtomicLong(0);

            @Override
            public void afterChunk(ChunkContext context) {
                long processed = processedCount.addAndGet(CHUNK_SIZE);
                log.info("사용자 데이터 삽입 진행률: ({}/{})", processed, USER_COUNT);
            }
        };
    }

    private List<UserEntity> generateUsers() {
        List<UserEntity> users = new ArrayList<>();

        for (int i = 1; i <= USER_COUNT; i++) {
            UserEntity user = UserEntity.builder()
                    .email("user" + i + "@example.com")
                    .nickname("사용자" + i)
                    .password("$2a$10$encrypted_password_hash")
                    .introduction(i + "번째 사용자의 자기소개")
                    .followerCount(0L)
                    .followeeCount(0L)
                    .build();
            users.add(user);
        }

        return users;
    }
}
