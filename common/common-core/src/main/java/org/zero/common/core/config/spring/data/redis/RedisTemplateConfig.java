package org.zero.common.core.config.spring.data.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 官网：
 * <a href="https://redis.io/">Redis</a>
 * <a href="https://spring.io/projects/spring-data-redis">Spring Data Redis</a>
 * 自动装配：{@link org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/10/20 13:36
 */
@Configuration(proxyBeanMethods = false)
public class RedisTemplateConfig {
    private static final String DEFAULT_REDIS_TEMPLATE_NAME = "strObjRedisTemplate";

    @Bean(name = DEFAULT_REDIS_TEMPLATE_NAME)
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    @ConditionalOnBean(ObjectMapper.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置Redis连接工厂
        template.setConnectionFactory(redisConnectionFactory);

        RedisSerializer<String> stringRedisSerializer = RedisSerializer.string();
        // RedisSerializer<Object> objectRedisSerializer = RedisSerializer.json();
        Jackson2JsonRedisSerializer<Object> objectRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        objectRedisSerializer.setObjectMapper(objectMapper);

        // 设置普通key和value序列化模式
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(objectRedisSerializer);
        // 设置hash key和value序列化模式
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(objectRedisSerializer);
        return template;
    }

    /**
     * 对字符串类型数据操作
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(@Qualifier(DEFAULT_REDIS_TEMPLATE_NAME) RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对哈希类型的数据操作
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(@Qualifier(DEFAULT_REDIS_TEMPLATE_NAME) RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对链表类型的数据操作
     */
    @Bean
    public ListOperations<String, Object> listOperations(@Qualifier(DEFAULT_REDIS_TEMPLATE_NAME) RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     */
    @Bean
    public SetOperations<String, Object> setOperations(@Qualifier(DEFAULT_REDIS_TEMPLATE_NAME) RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(@Qualifier(DEFAULT_REDIS_TEMPLATE_NAME) RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
