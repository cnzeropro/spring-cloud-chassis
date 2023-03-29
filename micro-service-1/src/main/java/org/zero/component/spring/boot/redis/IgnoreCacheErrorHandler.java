package org.zero.component.spring.boot.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * 忽略缓存异常，不影响业务流程
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2023/2/17
 */
@Slf4j
public final class IgnoreCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.warn(String.format("[%s] get key[%s] error", cache.getName(), key), exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.warn(String.format("[%s] put key[%s] error", cache.getName(), key), exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn(String.format("[%s] evict key[%s] error", cache.getName(), key), exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.warn(String.format("[%s] clear error", cache.getName()), exception);
    }
}
