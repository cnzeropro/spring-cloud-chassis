package org.zero.common.core.config.spring.boot.web.socket.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 自动装配：
 * {@link org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration}
 * {@link org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@EnableWebSocket
@Configuration(proxyBeanMethods = false)
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(/*new CustomWebSocketHandler()*/null, "/ws/custom", "/websocket/custom")
                // 允许跨域
                .setAllowedOrigins("*")
                // 握手处理器
                .setHandshakeHandler(/*new CustomHandshakeHandler()*/null)
                // 握手拦截器
                .addInterceptors(/*new CustomHandshakeInterceptor()*/null)
                // 启用 SockJS
                .withSockJS();
    }
}
