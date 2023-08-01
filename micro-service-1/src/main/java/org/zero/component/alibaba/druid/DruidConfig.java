package org.zero.component.alibaba.druid;

import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Optional;

/**
 * druid 配置
 * 自动装配参见 {@link com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure}
 *
 * @author zero
 */
@Configuration(proxyBeanMethods = false)
public class DruidConfig {
    private static final String DEFAULT_COMMON_JS_FILEPATH = "support/http/resources/js/common.js";

    /**
     * 去除监控页面底部的广告
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public FilterRegistrationBean<Filter> removeDruidAdFilterRegistrationBean(DruidStatProperties properties) {
        // 获取web监控页面的参数
        String pattern = Optional.ofNullable(properties.getStatViewServlet())
                .map(DruidStatProperties.StatViewServlet::getUrlPattern)
                .orElse("/druid/*");
        // 提取common.js的配置路径
        String commonJsPattern = pattern.replace("\\*", "js/common.js");
        // 创建filter进行过滤
        Filter filter = (request, response, chain) -> {
            chain.doFilter(request, response);
            // 重置缓冲区，响应头不会被重置
            response.resetBuffer();
            // 获取common.js
            String commonJsText = Utils.readFromResource(DEFAULT_COMMON_JS_FILEPATH);
            // 正则替换banner, 除去底部的广告信息
            commonJsText = commonJsText.replaceAll("<a.*?banner\"></a><br/>", "")
                    .replaceAll("powered.*?shrek.wang</a>", "");
            response.getWriter().write(commonJsText);
        };
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns(commonJsPattern);
        return filterRegistrationBean;
    }
}
