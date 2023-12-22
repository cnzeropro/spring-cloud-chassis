package org.zero.gateway.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zero.common.core.util.java.captcha.ArithmeticCaptchaCreator;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 验证码生成
 *
 * @author zero
 * @date 2018/7/5
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CaptchaHandlerFunction implements HandlerFunction<ServerResponse> {
    @Resource
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        MediaType imageJpeg = MediaType.IMAGE_JPEG;
        FastByteArrayOutputStream byteArrayOutputStream = new FastByteArrayOutputStream();
        String code = ArithmeticCaptchaCreator.creator()
            .createTexts()
            .createImg()
            .outAndGet(imageJpeg.getSubtype(), byteArrayOutputStream);
        request.queryParam("key")
            .ifPresent(key -> redisTemplate.opsForValue()
                .set("captcha:" + key, code, 3 * 60L, TimeUnit.SECONDS));
        return ServerResponse.status(HttpStatus.OK)
            .contentLength(byteArrayOutputStream.size())
            .contentType(imageJpeg)
            .cacheControl(CacheControl.noStore())
            .body(BodyInserters.fromResource(new ByteArrayResource(byteArrayOutputStream.toByteArray())));
    }

}
