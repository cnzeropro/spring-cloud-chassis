package org.zero.component.spring.boot.web.socket.tomcat;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
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
    private final ConcurrentMap<String, Session> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加session
     */
    public static Session add(String key, Session session) {
        return SESSION_POOL.put(key, session);
    }

    /**
     * 获得session
     */
    public static Session get(String key) {
        return SESSION_POOL.get(key);
    }

    /**
     * 删除session
     */
    public static Session remove(String key) {
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并关闭session
     */
    public static void removeAndClose(String key) {
        Session session = remove(key);
        if (Objects.nonNull(session)) {
            try {
                session.close();
            } catch (Exception e) {
                log.warn(String.format("Close Session error: %s", key), e);
            }
        }
    }
}
