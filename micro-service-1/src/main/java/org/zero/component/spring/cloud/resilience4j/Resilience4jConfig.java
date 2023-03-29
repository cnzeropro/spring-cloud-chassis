package org.zero.component.spring.cloud.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/12/1 14:41
 */
@Configuration(proxyBeanMethods = false)
public class Resilience4jConfig {
    /**
     * 时间限流：5s
     */
    private static final long DEFAULT_TIMEOUT = 5L;

    /**
     * 为所有断路器提供默认配置
     * 非响应式
     */
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> customizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(DEFAULT_TIMEOUT)).build())
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .build());
    }

    /**
     * 为所有断路器提供默认配置
     * 响应式
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> reactiveCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(DEFAULT_TIMEOUT)).build())
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .build());
    }
}
