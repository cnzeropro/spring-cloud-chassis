package org.zero.component.spring.boot.web.socket;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Slf4j
@UtilityClass
public class WsSessionManager {
    /**
     * session池
     */
    private final ConcurrentMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加session
     */
    public static WebSocketSession add(String key, WebSocketSession session) {
        return SESSION_POOL.put(key, session);
    }

    /**
     * 获得session
     */
    public static WebSocketSession get(String key) {
        return SESSION_POOL.get(key);
    }

    /**
     * 删除session
     */
    public static WebSocketSession remove(String key) {
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并关闭session
     */
    public static void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (Objects.nonNull(session)) {
            try {
                session.close();
            } catch (Exception e) {
                log.warn("close WebSocketSession error", e);
            }
        }
    }
}
