package org.zero.demo.spring.boot.web.socket.stomp.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.zero.demo.spring.boot.web.socket.stomp.WsUserPrincipal;

import java.security.Principal;
import java.util.Map;

/**
 * @author zero
 * @since 2021/8/24
 */
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = request.getHeaders().getFirst("token");
        // todo: 从token中获取用户消息
        // 构建Principal
        return new WsUserPrincipal("");
    }
}
