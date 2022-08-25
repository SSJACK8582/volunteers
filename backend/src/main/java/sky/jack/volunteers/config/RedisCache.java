package sky.jack.volunteers.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {
    @Resource
    public RedisTemplate<String, Object> redisTemplate;

    public void setCacheObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setCacheObject(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public Object getCacheObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean deleteObject(String key) {
        return redisTemplate.delete(key);
    }

    public long deleteObject(Collection collection) {
        return redisTemplate.delete(collection);
    }

    public Collection<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
