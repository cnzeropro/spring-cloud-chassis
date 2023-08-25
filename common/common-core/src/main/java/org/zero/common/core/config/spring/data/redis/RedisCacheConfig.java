package org.zero.common.core.config.spring.data.redis;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * 官网：
 * <a href="https://redis.io/">Redis</a>
 * <a href="https://spring.io/projects/spring-data-redis">Spring Data Redis</a>
 * 自动装配：{@link org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/10/20 13:36
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
public class RedisCacheConfig extends CachingConfigurerSupport {
    /**
     * redis缓存过期时间，默认6小时
     */
    private static final Integer REDIS_CACHE_TIMEOUT = 6;
    /**
     * 本地缓存过期时间，默认60分钟
     */
    private static final Integer LOCAL_CACHE_TIMEOUT = 60;
    /**
     * 初始的缓存空间大小，默认100
     */
    private static final Integer LOCAL_CACHE_INIT_CAPACITY = 100;
    /**
     * 缓存最大条数，默认1000
     */
    private static final Integer LOCAL_CACHE_MAX_SIZE = 1000;

    @Resource
    private RedisConnectionFactory connectionFactory;

    /**
     * 自定义缓存管理器
     */
    @Override
    public CacheManager cacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(REDIS_CACHE_TIMEOUT))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager
                .builder(RedisCacheWriter.lockingRedisCacheWriter(connectionFactory))
//                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }

    @Override
    public CacheResolver cacheResolver() {
        // 本地内存缓存管理器
        CacheManager caffeineCacheManager = getCaffeineCacheManager();
        // redis缓存管理器
        CacheManager redisCacheManager = cacheManager();
        // 优先读取堆内存缓存，堆内存缓存读取不到该key时再读取redis缓存
        return /*new CustomCacheResolver(ListUtil.of(caffeineCacheManager, redisCacheManager))*/null;
    }

    /**
     * 自定义缓存key生成器
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            String prefix = target.getClass().getName() + "#" + method.getName() + "(";
            StringJoiner sj = new StringJoiner(",", prefix, ")");
            Arrays.stream(params).map(String::valueOf).forEach(sj::add);
            return sj.toString();
        };
    }

    /**
     * 自定义缓存异常处理：忽略，不影响业务流程
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return /*new IgnoreCacheErrorHandler()*/null;
    }

    private CaffeineCacheManager getCaffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                // 最后一次访问后的过期过期
                .expireAfterAccess(LOCAL_CACHE_TIMEOUT, TimeUnit.MINUTES)
                // 初始缓存空间大小
                .initialCapacity(LOCAL_CACHE_INIT_CAPACITY)
                // 最大缓存条数
                .maximumSize(LOCAL_CACHE_MAX_SIZE));
        return caffeineCacheManager;
    }
}
