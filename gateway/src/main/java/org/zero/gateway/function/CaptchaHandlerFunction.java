/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zero.gateway.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zero.gateway.util.CaptchaUtil;
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
        // 生成验证码，并将验证码图片存入OutputStream
        FastByteArrayOutputStream byteArrayOutputStream = new FastByteArrayOutputStream();
        String code = CaptchaUtil.math(imageJpeg.getSubtype(), byteArrayOutputStream);
        // 拿取参数：验证码key
        String key = request.queryParam("key").orElse("default");
        // 保存验证码到redis，
        redisTemplate.opsForValue().set("captcha:" + key, code, 3 * 60L, TimeUnit.SECONDS);
        return ServerResponse.status(HttpStatus.OK)
                .contentType(imageJpeg)
                .body(BodyInserters.fromResource(new ByteArrayResource(byteArrayOutputStream.toByteArray())));
    }

}
