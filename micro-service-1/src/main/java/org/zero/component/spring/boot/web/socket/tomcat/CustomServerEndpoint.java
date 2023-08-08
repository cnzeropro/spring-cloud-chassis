package org.zero.component.spring.boot.web.socket.tomcat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zero.common.data.model.common.Result;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;

/**
 * ws 服务节点虽然需要使用 @Component 注入到 Spring 容器，但其本身还是多例的，如果想在本类注入其他对象就会报错。
 * 解决方案：
 * 1、通过 SpringUtil 等工具类获取 bean。
 * 2、通过实现某些 Aware 接口获取，比如ApplicationContextAware，BeanFactoryAware等等。
 *
 * @author zero
 * @since 2023/7/27
 */
@Slf4j
@Component
@ServerEndpoint(value = "/ws/custom/{id}", encoders = JsonEncoder.class, decoders = JsonDecoder.class)
public class CustomServerEndpoint {
    private static final TomcatWsSessionManager WS_SESSION_MANAGER = TomcatWsSessionManager.INSTANCE;

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        log.info("ws session is open: {}", session.getId());
        WS_SESSION_MANAGER.add(id, session);
    }

    /**
     * TextMessage
     */
    // @OnMessage
    // public void onMessage(Session session, @PathParam("id") String id, String message) {
    // }

    /**
     * TextMessage
     * 因为指定了编码、解码器，所以可以使用具体的pojo
     */
    @SneakyThrows
    @OnMessage
    public void onMessage(Session session, @PathParam("id") String id, CustomPojo pojo) {
        session.getBasicRemote().sendObject(Result.ok());
    }

    /**
     * BinaryMessage
     */
    // @OnMessage
    // public void onMessage(Session session, @PathParam("id") String id, byte[] message) {
    // }

    /**
     * BinaryMessage
     */
    @OnMessage
    public void onMessage(Session session, @PathParam("id") String id, ByteBuffer byteBuffer) {
    }

    @OnError
    public void onError(Session session, @PathParam("id") String id, Throwable error) {
        log.warn(String.format("ws session[%s] is error", session.getId()), error);
        WS_SESSION_MANAGER.removeAndClose(id);
    }

    @OnClose
    public void onClose(Session session, @PathParam("id") String id, CloseReason reason) {
        log.info("ws session[{}] is close: {}", session.getId(), reason);
        WS_SESSION_MANAGER.delete(id);
    }
}
