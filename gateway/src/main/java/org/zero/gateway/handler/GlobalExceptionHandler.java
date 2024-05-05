package org.zero.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.zero.common.data.model.vo.Result;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 网关通用异常处理器，只作用在 webflux 环境下，优先级低于 {@link org.springframework.web.server.handler.ResponseStatusExceptionHandler} 执行
 *
 * @author zero
 * @date 2020/4/28
 */
@Slf4j
@Order(-1)
@RequiredArgsConstructor
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    @Resource
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        log.warn(String.format("Error in path: %s", exchange.getRequest().getPath()), ex);
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            byte[] bytes = new byte[0];
            try {
                Result<Void> result = Result.fail(ex.getMessage());
                bytes= objectMapper.writeValueAsBytes(result);
            } catch (JsonProcessingException e) {
                log.warn("Writing response msg error", e);
            }
            return bufferFactory.wrap(bytes);
        }));
    }
}
