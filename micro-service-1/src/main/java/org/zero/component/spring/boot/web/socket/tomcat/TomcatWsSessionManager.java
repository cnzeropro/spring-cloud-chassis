package org.zero.component.spring.boot.web.socket.tomcat;

import lombok.extern.slf4j.Slf4j;
import org.zero.component.spring.boot.web.socket.WsSessionManager;

import javax.websocket.Session;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zero
 * @since 2022/8/3
 */
@Slf4j
public class TomcatWsSessionManager implements WsSessionManager<Session> {
    public static final TomcatWsSessionManager INSTANCE = new TomcatWsSessionManager();

    private TomcatWsSessionManager() {
    }

    /**
     * session池
     */
    private static final ConcurrentMap<String, Session> SESSION_POOL = new ConcurrentHashMap<>();

    @Override
    public Session add(String key, Session session) {
        return SESSION_POOL.put(key, session);
    }

    @Override
    public Session get(String key) {
        return SESSION_POOL.get(key);
    }

    @Override
    public Collection<Session> list() {
        return SESSION_POOL.values();
    }

    @Override
    public void delete(String key) {
        remove(key);
    }

    @Override
    public Session remove(String key) {
        return SESSION_POOL.remove(key);
    }

    @Override
    public void removeAndClose(String key) {
        Session session = remove(key);
        if (Objects.nonNull(session)) {
            try {
                session.close();
            } catch (Exception e) {
                log.warn(String.format("Close Session error: %s", key), e);
            }
        }
    }

    @Override
    public void clear() {
        SESSION_POOL.clear();
    }
}
