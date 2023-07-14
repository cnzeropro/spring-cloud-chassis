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

package org.zero.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.zero.gateway.function.CaptchaHandlerFunction;

/**
 * 路由配置
 *
 * @author zero
 * @date 2021-06-11
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RouterFunctionConfig {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(CaptchaHandlerFunction captchaHandlerFunction) {
        return RouterFunctions.route(RequestPredicates.path("/captcha")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                captchaHandlerFunction);
    }
}
