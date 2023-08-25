package org.zero.common.core.config.alibaba.druid;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Optional;

/**
 * 官网：<a href="https://github.com/alibaba/druid/">Druid</a>
 * 自动装配：{@link DruidDataSourceAutoConfigure}
 *
 * @author zero
 */
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
@EnableConfigurationProperties(DruidStatProperties.class)
@Configuration(proxyBeanMethods = false)
public class DruidConfig {
    private static final String COMMON_JS_PATH = "js/common.js";
    private static final String COMMON_JS_RESOURCE_PATH = "support/http/resources/" + COMMON_JS_PATH;

    /**
     * 去除监控页面底部的广告
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(name = "spring.datasource.druid.stat-view-servlet.enabled", havingValue = "true")
    public FilterRegistrationBean<Filter> delDruidAdFilterRegistrationBean(DruidStatProperties properties) {
        // 获取web监控页面的参数
        String pattern = Optional.ofNullable(properties.getStatViewServlet())
                .map(DruidStatProperties.StatViewServlet::getUrlPattern)
                .orElse("/druid/*");
        // 提取common.js的配置路径
        String commonJsPattern = pattern.replace("\\*", COMMON_JS_PATH);
        // 创建filter进行过滤
        Filter filter = (request, response, chain) -> {
            chain.doFilter(request, response);
            // 重置缓冲区，响应头不会被重置
            response.resetBuffer();
            // 获取common.js文件内容
            String commonJsText = Utils.readFromResource(COMMON_JS_RESOURCE_PATH);
            // 正则替换banner，除去底部的广告信息
            String replacedCommonJsText = commonJsText.replaceAll("<a.*?banner\"></a><br/>", "")
                    .replaceAll("powered.*?shrek.wang</a>", "");
            response.getWriter().write(replacedCommonJsText);
        };
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns(commonJsPattern);
        return filterRegistrationBean;
    }
}
