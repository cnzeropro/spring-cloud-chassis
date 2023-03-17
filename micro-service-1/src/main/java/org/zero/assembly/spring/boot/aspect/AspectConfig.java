package org.zero.assembly.spring.boot.aspect;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author zero
 * @since 2021/2/20
 */
@Configuration(proxyBeanMethods = false)
// proxyTargetClass：是否启用cglib代理；exposeProxy：是否暴露代理，可使用AopContext.currentProxy()获取当前代理对象
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class AspectConfig {
}
