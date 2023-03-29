package org.zero.component.spring.cloud.loadbalancer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/12/1 14:41
 */
@Configuration(proxyBeanMethods = false)
public class LoadbalancerConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
