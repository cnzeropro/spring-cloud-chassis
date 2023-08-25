package org.zero.common.core.config.spring.netflix.zuul;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://github.com/Netflix/zuul">Zuul</a>
 * 自动装配：
 * {@link org.springframework.cloud.netflix.zuul.ZuulServerAutoConfiguration}
 * {@link org.springframework.cloud.netflix.zuul.ZuulProxyAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/9/8 16:41
 */
@Configuration(proxyBeanMethods = false)
@EnableZuulServer
@EnableZuulProxy
public class ZuulConfig {
}
