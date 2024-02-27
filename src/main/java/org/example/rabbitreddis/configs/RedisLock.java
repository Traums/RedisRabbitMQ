package org.example.rabbitreddis.configs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Log4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class RedisLock {
    RedisTemplate<String,Long> redisTemplate;
    private static final String KEY_TEMPLATE = "lock:%s";

    public boolean acquireLock(long expireMillis, String taskKey){
        String lockKey = getLockKey(taskKey);
        Long expireAt = redisTemplate.opsForValue().get(lockKey);
        long currentTimeMillis = System.currentTimeMillis();

        if(Objects.nonNull(expireAt)){
            if(expireAt <= currentTimeMillis){
                redisTemplate.delete(lockKey);
            }
            else {
                return false;
            }
        }
        Long expire = currentTimeMillis + expireMillis;
        return Optional
                .ofNullable(redisTemplate.opsForValue().setIfAbsent(lockKey,expire)).orElse(false);
    }
    public void releaseLock(String taskKey){
        String lockKey = getLockKey(taskKey);
        redisTemplate.delete(lockKey);
    }
    private String getLockKey(String key) {
        return String.format(KEY_TEMPLATE,key);
    }
}
