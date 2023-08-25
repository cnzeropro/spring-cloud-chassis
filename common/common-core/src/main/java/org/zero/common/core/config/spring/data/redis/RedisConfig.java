package org.zero.common.core.config.spring.data.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;

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
public class RedisConfig {
    @Bean
    public DefaultRedisScript<Long> limitRedisScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    /**
     * 限流脚本
     */
    private String limitScriptText() {
        return "local key = KEYS[1]\n" +
                "local count = tonumber(ARGV[1])\n" +
                "local time = tonumber(ARGV[2])\n" +
                "local current = redis.call('get', key);\n" +
                "if current and tonumber(current) > count then\n" +
                "    return tonumber(current);\n" +
                "end\n" +
                "current = redis.call('incr', key)\n" +
                "if tonumber(current) == 1 then\n" +
                "    redis.call('expire', key, time)\n" +
                "end\n" +
                "return tonumber(current);";
    }
}
