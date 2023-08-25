package org.zero.common.core.config.spring.netflix.eureka;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://github.com/Netflix/eureka">Eureka</a>
 * 自动装配：
 * {@link org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration}
 * {@link org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration}
 *
 * @author zero
 * @since 2021/2/14
 */

@EnableDiscoveryClient
// 请区分服务端和客户端来使用注解
// 服务端
@EnableEurekaServer
// 客户端
@EnableEurekaClient
@Configuration(proxyBeanMethods = false)
public class EurekaConfig {
}
