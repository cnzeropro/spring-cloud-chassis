package org.zero.common.core.config.spring.alibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 官网：<a href="https://sentinelguard.io/zh-cn/index.html">Sentinel</a>
 * 自动装配：
 * {@link com.alibaba.cloud.sentinel.custom.SentinelAutoConfiguration}
 * {@link com.alibaba.cloud.sentinel.SentinelWebAutoConfiguration}
 * {@link com.alibaba.cloud.sentinel.SentinelWebFluxAutoConfiguration}
 *
 * @author Zero
 * @since 2022/7/16
 */
@Configuration(proxyBeanMethods = false)
public class SentinelConfig {

    /**
     * 解决流控链路不生效的问题，需要引入 sentinel-web-servlet 包
     */
    @Bean
    public FilterRegistrationBean<Filter> sentinelFilterRegistration() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CommonFilter());
        // 入口资源关闭聚合，解决流控链路不生效的问题
        registrationBean.addInitParameter(CommonFilter.WEB_CONTEXT_UNIFY, "false");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("sentinelFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
