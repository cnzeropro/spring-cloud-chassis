package org.zero.common.core.config.spring.boot.web.socket.stomp;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

/**
 * 自动装配：{@link org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration}
 *
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
                .addInterceptors(/*new CustomHandshakeInterceptor()*/null)
                // 握手处理
                .setHandshakeHandler(/*new CustomHandshakeHandler()*/null)
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
        registration.interceptors(/*new CustomChannelInterceptor()*/null);
    }

    /**
     * 输出通道参数设置（可选）
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4).maxPoolSize(8).queueCapacity(16).keepAliveSeconds(60);
        registration.interceptors(/*new CustomChannelInterceptor()*/null);
    }
}