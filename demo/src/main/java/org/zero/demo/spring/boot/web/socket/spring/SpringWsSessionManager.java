package org.zero.demo.spring.boot.web.socket.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;
import org.zero.demo.spring.boot.web.socket.WsSessionManager;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zero
 * @since 2022/8/3
 */
@Slf4j
public class SpringWsSessionManager implements WsSessionManager<WebSocketSession> {
    public static final SpringWsSessionManager INSTANCE = new SpringWsSessionManager();

    private SpringWsSessionManager() {
    }

    /**
     * session池
     */
    private static final ConcurrentMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    @Override
    public WebSocketSession add(String key, WebSocketSession session) {
        return SESSION_POOL.put(key, session);
    }

    @Override
    public WebSocketSession get(String key) {
        return SESSION_POOL.get(key);
    }

    @Override
    public Collection<WebSocketSession> list() {
        return SESSION_POOL.values();
    }

    @Override
    public void delete(String key) {
        remove(key);
    }

    @Override
    public WebSocketSession remove(String key) {
        return SESSION_POOL.remove(key);
    }

    @Override
    public void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (Objects.nonNull(session)) {
            try {
                session.close();
            } catch (Exception e) {
                log.warn(String.format("Close WebSocketSession error: %s", key), e);
            }
        }
    }

    @Override
    public void clear() {
        SESSION_POOL.clear();
    }
}
