package org.zero.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.zero.gateway.config.props.SwaggerDocProperties;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * swagger开启basic认证
 *
 * @author zero
 * @date 2021/9/12
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "swagger.basic.enabled")
public class SwaggerBasicGlobalFilter implements GlobalFilter {
    private static final String API_URI = "/v3/api-docs";
    private static final String BASIC_PREFIX = "Basic ";

    @Resource
    private final SwaggerDocProperties swaggerProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 不是相关URI，放行
        if (!request.getURI().getPath().contains(API_URI)) {
            return chain.filter(exchange);
        }

        // 有授权，放行
        if (hasAuth(exchange)) {
            return chain.filter(exchange);
        }

        // 无basic认证返回401
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.WWW_AUTHENTICATE, "Basic Realm=\"zero\"");
        return response.setComplete();

    }

    /**
     * 简单的basic认证
     */
    private boolean hasAuth(ServerWebExchange exchange) {
        String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Basic certification information: {}", auth);

        if (!StringUtils.hasText(auth) || !auth.startsWith(BASIC_PREFIX)) {
            return false;
        }

        String username = swaggerProperties.getBasic().getUsername();
        String password = swaggerProperties.getBasic().getPassword();
        String srcStr = username + ":" + password;
        String encoded = Base64Utils.encodeToString(srcStr.getBytes(StandardCharsets.UTF_8));
        return auth.equals(BASIC_PREFIX + encoded);
    }
}
