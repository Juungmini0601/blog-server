package org.logly.batch.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

import org.logly.batch.model.PostData;

@Slf4j
@Configuration
public class TestPostDataGenerationBatchConfig {

    private static final int CHUNK_SIZE = 1000;
    private static final int USER_COUNT = 200_000;
    private static final int POST_COUNT = 4_000_000;
    private static final Random random = new Random();

    @Bean
    public Job postDataGenerationJob(JobRepository jobRepository, Step createPostsStep) {
        return new JobBuilder("postDataGenerationJob", jobRepository)
                .start(createPostsStep)
                .build();
    }

    @Bean
    public Step createPostsStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<PostData> postReader,
            ItemWriter<PostData> postWriter) {
        return new StepBuilder("createPostsStep", jobRepository)
                .<PostData, PostData>chunk(CHUNK_SIZE, transactionManager)
                .reader(postReader)
                .writer(postWriter)
                .listener(postChunkListener())
                .build();
    }

    @Bean
    @StepScope // Step 실행 시점에 Bean 생성
    public ItemReader<PostData> postReader() {
        List<PostData> posts = generatePosts();
        return new ListItemReader<>(posts);
    }

    @Bean
    @StepScope
    public ItemWriter<PostData> postWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<PostData>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO posts (title, content, thumbnail_url, is_public, view_count, like_count,"
                        + " comment_count, series_id, user_id, created_at, updated_at) VALUES (:title, :content,"
                        + " :thumbnailUrl, :isPublic, :viewCount, :likeCount, :commentCount, :seriesId, :userId,"
                        + " NOW(), NOW())")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    @StepScope
    public ChunkListener postChunkListener() {
        return new ChunkListener() {
            private final AtomicLong processedCount = new AtomicLong(0);

            @Override
            public void afterChunk(ChunkContext context) {
                long processed = processedCount.addAndGet(CHUNK_SIZE);
                log.info("게시글 데이터 삽입 진행률: ({}/{})", processed, POST_COUNT);
            }
        };
    }

    private List<PostData> generatePosts() {
        List<PostData> posts = new ArrayList<>();

        for (int i = 1; i <= POST_COUNT; i++) {
            PostData postData = new PostData();
            postData.setTitle(i + "번째 테스트 게시글 제목 입니다.");
            postData.setContent(i + "번째 테스트 게시글 내용입니다.");
            postData.setThumbnailUrl(null);
            postData.setIsPublic(true);
            postData.setViewCount(0L);
            postData.setLikeCount(0L);
            postData.setCommentCount(0L);
            postData.setSeriesId(null);

            // 1에서 200,000 사이의 랜덤 유저 ID 설정
            long userId = random.nextInt(USER_COUNT) + 1L;
            postData.setUserId(userId);
            posts.add(postData);
        }

        return posts;
    }
}
