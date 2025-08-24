package org.logly.redis.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.logly.error.CustomException;
import org.logly.error.ErrorType;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void set(String key, Object value, long ttlSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
    }

    public <T> Optional<T> get(String key, Class<T> classType) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(objectMapper.writeValueAsString(value), classType));
        } catch (Exception e) {
            throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public <T> Optional<List<T>> getList(String key, Class<T> elementType) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return Optional.empty();
        }

        try {
            List<T> convertedValue = objectMapper.convertValue(
                    value, objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
            return Optional.of(convertedValue);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean tryAcquireSemaphore(String key, int maxConcurrent, int ttlSeconds) {
        Long current = redisTemplate.opsForValue().increment(key);

        if (current != null && current == 1) {
            redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
        }

        if (current == null || current > maxConcurrent) {
            redisTemplate.opsForValue().decrement(key);
            return false;
        }

        return true;
    }

    public void releaseSemaphore(String key) {
        redisTemplate.opsForValue().decrement(key);
    }
}
