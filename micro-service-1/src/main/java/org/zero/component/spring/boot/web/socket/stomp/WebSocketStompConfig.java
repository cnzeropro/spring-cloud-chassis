package org.zero.component.spring.boot.web.socket.stomp;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2023/2/5
 */
@EnableWebSocketMessageBroker
@Configuration(proxyBeanMethods = false)
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 注册stomp的端点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")
                // 允许跨域
                .setAllowedOrigins("*")
                // 握手拦截
                .addInterceptors(new CustomHandshakeInterceptor())
                // 握手处理
                .setHandshakeHandler(new CustomHandshakeHandler())
                // 启用 SockJS
                .withSockJS();
        registry.setErrorHandler(new StompSubProtocolErrorHandler());
    }

    /**
     * 配置消息代理（中继器、路由、消息中介）
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 可订阅Broker名称
        // 本地内存模式
        registry.enableSimpleBroker("/queue/", "/topic")
                // 心跳频率：10分钟
                .setHeartbeatValue(new long[]{10 * 60 * 1000L, 10 * 60 * 1000L});
        // 三方中间件模式：如mq
        // registry.enableStompBrokerRelay("/queue/", "/topic/")
        //         .setRelayHost()
        //         .setRelayPort()
        //         .setClientLogin()
        //         .setClientPasscode();

        // 全局使用的消息前缀
        registry.setApplicationDestinationPrefixes("/app/");
        // 点对点使用的订阅前缀
        registry.setUserDestinationPrefix("/user");
    }


    /**
     * 消息传输参数配置（可选）
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        // 消息大小限制（字节）
        registry.setMessageSizeLimit(8192)
                // 消息缓存大小限制（字节）
                .setSendBufferSizeLimit(8192)
                // 消息发送时间限制（毫秒）
                .setSendTimeLimit(10000);
    }

    /**
     * 输入通道参数设置（可选）
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4).maxPoolSize(8).queueCapacity(16).keepAliveSeconds(60);
        registration.interceptors(new CustomChannelInterceptor());
    }

    /**
     * 输出通道参数设置（可选）
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4).maxPoolSize(8).queueCapacity(16).keepAliveSeconds(60);
        registration.interceptors(new CustomChannelInterceptor());
    }

    /* *************************************************** 内部类 *************************************************** */

    public static class CustomHandshakeHandler extends DefaultHandshakeHandler {
        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            String token = request.getHeaders().getFirst("token");
            // todo: 从token中获取用户消息
            // 构建Principal
            return new WsUserPrincipal("");
        }
    }

    public static class CustomHandshakeInterceptor implements HandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

        }
    }

    public static class CustomChannelInterceptor implements ChannelInterceptor {
        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (Objects.nonNull(accessor)) {
                // 在http阶段，websocket之前就做了认证的封装，所以这里直接取信息
                // 当然在此处也可以封装认证用户的信息
                Principal principal = accessor.getUser();
                String token = accessor.getFirstNativeHeader("token");
                StompCommand command = accessor.getCommand();
                if (StompCommand.CONNECT.equals(command)) {
                    // 连接

                } else if (StompCommand.DISCONNECT.equals(command)) {
                    // 断开连接

                }
            }
            return message;
        }
    }
}