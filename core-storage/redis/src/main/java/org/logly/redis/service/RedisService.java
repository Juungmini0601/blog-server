package org.logly.redis.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long ttlSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
    }

    public <T> Optional<T> get(String key, Class<T> classType) {
        Object value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(classType.cast(value));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
