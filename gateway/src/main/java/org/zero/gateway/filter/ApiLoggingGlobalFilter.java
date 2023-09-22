package org.zero.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;

/**
 * 接口请求日志记录过滤器
 * <p>
 * 全局拦截器，作用所有的微服务接口
 *
 * @author zero
 * @date 2020/7/11
 */
@Slf4j
@Component
public class ApiLoggingGlobalFilter implements GlobalFilter, Ordered {
    private static final String START_TIME = "startTime";
    private static final String X_REAL_IP = "X-Real-IP";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        ServerHttpResponse httpResponse = exchange.getResponse();
        // 请求相关参数日志记录
        URI uri = httpRequest.getURI();
        if (log.isDebugEnabled()) {
            log.debug(String.format("Host: {%s}, Method: {%s}, Path: {%s}, Query: {%s}",
                    uri.getHost(), httpRequest.getMethodValue(),
                    httpRequest.getPath().pathWithinApplication(), httpRequest.getQueryParams()));
        }
        exchange.getAttributes().put(START_TIME, System.nanoTime());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long endTime = System.nanoTime();
            Long startTime = exchange.getAttribute(START_TIME);
            if (Objects.nonNull(startTime)) {
                // 响应相关参数日志记录
                if (log.isDebugEnabled()) {
                    log.debug("Api[{}] from IP: {}, response status code: {}, time-costing: {}",
                            uri.getRawPath(), httpRequest.getHeaders().getFirst(X_REAL_IP),
                            httpResponse.getRawStatusCode(), Duration.ofNanos(endTime - startTime));
                }
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
