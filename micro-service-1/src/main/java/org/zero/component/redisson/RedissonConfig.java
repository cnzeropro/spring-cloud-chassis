package org.zero.component.redisson;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Redisson主要作用：
 * 1、缓存（RMap）
 * 2、对象存储（RBucket）
 * 3、分布式锁（RLock）
 * 4、布隆过滤器（RBloomFilter）
 * 5、限流器（RRateLimiter）
 * 6、分布式集合（RMap，RMultimap，RSet，RList，RDeque，...）
 * 7、分布式对象（RAtomicLong，RAtomicDouble，RTopic，...）
 * <p>
 * redisson-spring-boot-starter已自动装配了部分bean
 * 具体配置请使用：spring.redis.redisson.file=classpath:redisson.yml
 *
 * @author zero
 * @since 2022/2/16
 */
@Configuration(proxyBeanMethods = false)
public class RedissonConfig {
    @Resource
    private RedissonClient redissonClient;

    /**
     * Redisson和Spring Cache框架整合使用
     * 注意使用@EnableCaching注解开启缓存
     * 详情配置参见：classpath:/redisson/cache-config.json或者yml
     */
    @Bean
    public CacheManager cacheManager() throws IOException {
        // RedissonSpringClusteredLocalCachedCacheManager仅限于Redisson PRO版本
        return new RedissonSpringCacheManager(redissonClient, "classpath:/cache-config.json");
    }

    /**
     * 注入全局布隆过滤器
     */
    @Bean
    public RBloomFilter<String> bloomFilter() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("bloom");
        // 初始化布隆过滤器，预计统计元素数量为10^8，期望误差率为0.003
        bloomFilter.tryInit((long) 1E8, 0.003);
        return bloomFilter;
    }
}
