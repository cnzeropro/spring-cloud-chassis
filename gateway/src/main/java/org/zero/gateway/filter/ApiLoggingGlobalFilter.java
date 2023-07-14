package org.zero.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    // nginx 添加的原始 IP 头
    private static final String X_REAL_IP = "X-Real-IP";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        ServerHttpResponse httpResponse = exchange.getResponse();

        if (log.isDebugEnabled()) {
            log.debug(String.format("Method: {%s}, Host: {%s}, Path: {%s}, Query: {%s}",
                    httpRequest.getMethod().name(), httpRequest.getURI().getHost(),
                    httpRequest.getURI().getPath(), httpRequest.getQueryParams()));
        }
        exchange.getAttributes().put(START_TIME, System.nanoTime());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (Objects.nonNull(startTime)) {
                long executeTime = System.nanoTime() - startTime;
                List<String> ips = httpRequest.getHeaders().get(X_REAL_IP);
                String ip = CollectionUtils.isEmpty(ips) ? null : ips.get(0);
                String api = httpRequest.getURI().getRawPath();

                int code = Optional.ofNullable(httpResponse.getStatusCode()).map(HttpStatus::value).orElse(500);
                // 当前仅记录日志，后续考虑其他处理
                if (log.isDebugEnabled()) {
                    log.debug("Api[{}] from IP: {}, response status code: {}, request time: {}", api, ip, code, Duration.ofNanos(executeTime));
                }
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
