package org.zero.component.spring.boot.web.socket.tomcat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author yufa.wang (yufa.wang@ronganchina.com)
 * @since 2023/7/27
 */
@Slf4j
@Component
@ServerEndpoint(value = "/ws/custom/{id}")
public class CustomServerEndpoint {
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        log.info("建立ws连接");
        WsSessionManager.add(session.getId(), session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.warn("异常", error);
        WsSessionManager.removeAndClose(session.getId());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("关闭ws连接，CloseReason：{}", closeReason);
        WsSessionManager.removeAndClose(session.getId());
    }
}
