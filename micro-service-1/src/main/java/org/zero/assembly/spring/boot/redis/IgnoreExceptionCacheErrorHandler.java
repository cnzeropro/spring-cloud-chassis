package org.zero.assembly.spring.boot.redis;

import cn.hutool.core.util.StrUtil;
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
public final class IgnoreExceptionCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.warn(StrUtil.format("[{}] get key[{}] error", cache.getName(), key), exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.warn(StrUtil.format("[{}] put key[{}] error", cache.getName(), key), exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn(StrUtil.format("[{}] evict key[{}] error", cache.getName(), key), exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.warn(StrUtil.format("[{}] clear error", cache.getName()), exception);
    }
}
