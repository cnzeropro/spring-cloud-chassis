package org.zero.component.spring.boot.web.socket;

import java.util.Collection;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
public interface WsSessionManager<T> {
    /**
     * 添加session
     */
    T add(String key, T session);

    /**
     * 获取session
     */
    T get(String key);

    /**
     * 获取全部session
     */
    Collection<T> list();

    /**
     * 删除session
     */
    void delete(String key);

    /**
     * 删除session
     */
    T remove(String key);

    /**
     * 删除并关闭session
     */
    void removeAndClose(String key);

    /**
     * 删除全部session
     */
    void clear();
}
