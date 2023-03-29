package org.zero.component.spring.boot.redis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 忽略缓存异常，不影响业务流程
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2023/2/17
 */
@AllArgsConstructor
public final class CustomCacheResolver implements CacheResolver {
    private List<CacheManager> cacheManagerList;

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<String> cacheNames = context.getOperation().getCacheNames();
        if (CollUtil.isEmpty(cacheNames)) {
            return Collections.emptyList();
        }
        Collection<Cache> result = ListUtil.list(false);
        for (CacheManager cacheManager : cacheManagerList) {
            for (String cacheName : cacheNames) {
                Cache cache = cacheManager.getCache(cacheName);
                if (Objects.isNull(cache)) {
                    throw new IllegalArgumentException(StrUtil.format("Can not find cache named[{}] for [{}]", cacheName, context.getOperation()));
                }
                result.add(cache);
            }
        }
        return result;
    }
}