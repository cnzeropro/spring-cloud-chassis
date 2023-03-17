package org.zero.assembly.spring.alibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author Zero
 * @since 2022/7/16
 */
@Configuration(proxyBeanMethods = false)
public class SentinelConfig {

    /**
     * 解决流控链路不生效的问题，需要引入 sentinel-web-servlet 包
     *
     * @return
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
