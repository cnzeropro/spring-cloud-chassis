package org.zero.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.zero.gateway.config.props.GatewayProperties;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author zero
 * @date 2019/2/1
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(GatewayProperties.class)
@Component
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory<Object> {
    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();
    private static final String PASSWORD = "password";

    private final GatewayProperties gatewayConfig;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            // 1. 不是登录请求，通过
            if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), "/oauth2/token")) {
                return chain.filter(exchange);
            }

            // 2. 刷新token类型，通过
            String grantType = request.getQueryParams().getFirst("grant_type");
            if (Objects.equals("refresh_token", grantType)) {
                return chain.filter(exchange);
            }

            // 3. 前端加密密文解密逻辑
            ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);
            // 4. 解密生成新的报文
            Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(decryptAES());
            BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            headers.remove(HttpHeaders.CONTENT_LENGTH);

            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
            return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
                ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
                return chain.filter(exchange.mutate().request(decorator).build());
            }));
        };
    }

    /**
     * 原文解密
     */
    private Function<String, Mono<String>> decryptAES() {
        return s -> {
            AES aes = SecureUtil.aes(gatewayConfig.getEncodeKey().getBytes(StandardCharsets.UTF_8));
            Map<String, String> paramMap = HttpUtil.decodeParamMap(s, StandardCharsets.UTF_8);
            String password = paramMap.get(PASSWORD);
            if (StringUtils.hasText(password)) {
                paramMap.put(PASSWORD, aes.decryptStr(password));
            } else {
                log.error("Unlawful request param: {}", s);
            }
            return Mono.just(HttpUtil.toParams(paramMap, StandardCharsets.UTF_8, true));
        };
    }

    /**
     * 报文转换
     */
    private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

}
