package org.zero.common.core.config.alibaba.jetcache;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://github.com/alibaba/jetcache/">JetCache</a>
 * 自动装配：{@link com.alicp.jetcache.autoconfigure.JetCacheAutoConfiguration}
 *
 * @author zero
 * @since 2023/8/21
 */
@EnableMethodCache(basePackages = "org.zero.cache")
// Deprecated，请使用CacheManager相关api
// @EnableCreateCacheAnnotation
@Configuration(proxyBeanMethods = false)
public class JetCacheConfig {
}
