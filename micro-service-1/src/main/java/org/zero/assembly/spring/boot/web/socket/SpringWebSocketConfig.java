package org.zero.assembly.spring.boot.web.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer {

    @Resource
    private CustomWsHandler customWsHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customWsHandler, "/ws", "/webSocket")
                //允许跨域
                .setAllowedOrigins("*");
    }
}
