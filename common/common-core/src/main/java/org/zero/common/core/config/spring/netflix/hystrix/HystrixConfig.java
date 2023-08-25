package org.zero.common.core.config.spring.netflix.hystrix;

import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://github.com/Netflix/Hystrix">Hystrix</a>
 * 自动装配：{@link org.springframework.cloud.netflix.hystrix.HystrixAutoConfiguration}
 *
 * @author zero
 * @since 2021/6/23
 */
// 从 3.0.1 版本开始，Hystrix已从Spring Cloud Netflix中删除，因此@EnableCircuitBreaker也被一并移除了
// @EnableCircuitBreaker
@EnableHystrix
@Configuration(proxyBeanMethods = false)
public class HystrixConfig {
}
