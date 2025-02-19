package com.hongjunland.bbstemplate.post.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTestService {
    private final RedisTemplate<String, String> redisTemplate;
    @PostConstruct
    public void testRedis() {
        redisTemplate.opsForValue().set("testKey", "Hello, Redis!", 10, TimeUnit.SECONDS);
        String value = redisTemplate.opsForValue().get("testKey");
        System.out.println("Redis에서 가져온 값: " + value);
    }
}
