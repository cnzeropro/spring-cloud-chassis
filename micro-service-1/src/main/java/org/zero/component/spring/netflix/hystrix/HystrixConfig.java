package org.zero.component.spring.netflix.hystrix;

import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;

/**
 * @author zero
 * @since 2021/6/23
 */
@Configuration(proxyBeanMethods = false)
// 从 3.0.1 版本开始，Hystrix已从Spring Cloud Netflix中删除，因此@EnableCircuitBreaker也被一并移除了
// @EnableCircuitBreaker
@EnableHystrix
public class HystrixConfig {
}
