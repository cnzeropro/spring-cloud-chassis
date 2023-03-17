package org.zero.assembly.spring.netflix.zuul;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/9/8 16:41
 */
@Configuration(proxyBeanMethods = false)
@EnableZuulServer
@EnableZuulProxy
public class ZuulConfig {
}
