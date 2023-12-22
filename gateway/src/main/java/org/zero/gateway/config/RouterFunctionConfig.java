package org.zero.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        return RouterFunctions.route()
            .GET("/captcha", captchaHandlerFunction)
            .build();
    }
}
