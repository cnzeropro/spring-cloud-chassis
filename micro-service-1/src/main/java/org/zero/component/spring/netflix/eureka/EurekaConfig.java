package org.zero.component.spring.netflix.eureka;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author zero
 * @since 2021/2/14
 */
@Configuration(proxyBeanMethods = false)
// 请区分服务端和客户端来使用注解
//@EnableEurekaServer
@EnableEurekaClient
@EnableDiscoveryClient
public class EurekaConfig {
}
