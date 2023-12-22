package org.zero.gateway.filter;

import cn.hutool.core.text.CharSequenceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.zero.common.data.model.Result;
import org.zero.gateway.config.props.GatewayProperties;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * The type Validate code gateway filter.
 *
 * @author lengleng
 * @date 2018 /7/4 验证码处理
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(GatewayProperties.class)
@Component
public class ValidateCaptchaGatewayFilter extends AbstractGatewayFilterFactory<Object> {
    @Resource
    private final GatewayProperties configProperties;
    @Resource
    private final ObjectMapper objectMapper;
    @Resource
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            boolean isAuthToken = CharSequenceUtil.containsAnyIgnoreCase(request.getURI().getPath(), "/oauth2/token");

            // 不是登录请求，放行
            if (!isAuthToken) {
                return chain.filter(exchange);
            }

            // 刷新token请求，放行
            String grantType = request.getQueryParams().getFirst("grant_type");
            if (Objects.equals("refresh_token", grantType)) {
                return chain.filter(exchange);
            }

            String clientId = request.getHeaders().getFirst("client_id");
            boolean isIgnoreClient = configProperties.getIgnoreClients().contains(clientId);
            try {
                // 只有 OAuth 和不在忽略客户端中的请求需要检查代码
                if (!isIgnoreClient) {
                    checkCaptcha(request);
                }
            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                final String errMsg = e.getMessage();
                return response.writeWith(Mono.create(monoSink -> {
                    try {
                        byte[] bytes = objectMapper.writeValueAsBytes(Result.fail(errMsg));
                        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
                        monoSink.success(dataBuffer);
                    } catch (JsonProcessingException ex) {
                        log.error("Writing response msg error", ex);
                        monoSink.error(ex);
                    }
                }));
            }

            return chain.filter(exchange);
        };
    }

    private void checkCaptcha(ServerHttpRequest request) {
        String code = request.getQueryParams().getFirst("code");

        if (!StringUtils.hasText(code)) {
            throw new IllegalArgumentException("验证码不能为空");
        }

        String key = request.getQueryParams().getFirst("key");
        String redisKey = "captcha:" + key;
        Object redisCode = redisTemplate.opsForValue().get(redisKey);
        if (!code.equals(redisCode)) {
            throw new IllegalArgumentException("验证码不合法");
        }

        redisTemplate.delete(redisKey);
    }
}
