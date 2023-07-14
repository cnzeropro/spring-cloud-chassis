package org.zero.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * 网关配置
 *
 * @author zero
 * @date 2022/10/8
 */
@Configuration(proxyBeanMethods = false)
public class GatewayConfig {
    /**
     * 路由限流配置-key
     *
     * @see <a href="https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory">Spring Cloud Gateway</a>
     */
    @Bean
    public KeyResolver keyResolver() {
        return exchange -> Mono.just(Optional.of(exchange.getRequest())
                .map(ServerHttpRequest::getRemoteAddress)
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress)
                .orElseThrow(IllegalArgumentException::new));
    }
}
