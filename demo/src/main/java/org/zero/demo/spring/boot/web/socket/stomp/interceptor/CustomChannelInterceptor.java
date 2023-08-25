package org.zero.demo.spring.boot.web.socket.stomp.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;
import java.util.Objects;

/**
 * @author zero
 * @since 2021/8/24
 */
public class CustomChannelInterceptor implements ChannelInterceptor {
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
