package org.zero.demo.spring.boot.web.socket.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * ws消息处理类
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Slf4j
public class CustomWebSocketHandler extends AbstractWebSocketHandler {
    private static final SpringWsSessionManager WS_SESSION_MANAGER = SpringWsSessionManager.INSTANCE;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("ws session established");
        WS_SESSION_MANAGER.add(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("handle text message");
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.info("handle binary message");
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("handle pong message");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.warn("ws session error", exception);
        WS_SESSION_MANAGER.removeAndClose(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("ws session closed: {}", status);
        WS_SESSION_MANAGER.delete(session.getId());
    }
}
