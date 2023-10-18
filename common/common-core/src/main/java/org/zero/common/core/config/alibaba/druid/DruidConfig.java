package org.zero.common.core.config.alibaba.druid;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
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
    public FilterRegistrationBean<Filter> delDruidAdFilterRegistrationBean(DruidStatProperties properties) throws IOException {
        // 获取web监控页面的参数
        String pattern = Optional.ofNullable(properties.getStatViewServlet())
                .map(DruidStatProperties.StatViewServlet::getUrlPattern)
                .orElse("/druid/*");
        // 提取common.js的配置路径
        String commonJsPattern = pattern.replace("\\*", COMMON_JS_PATH);
        // 获取common.js文件内容
        String commonJs = Utils.readFromResource(COMMON_JS_RESOURCE_PATH);

        // 广告去除方案2选1
        // 1、正则替换banner，除去底部的广告信息
        // String newCommonJs = commonJs.replaceAll("<a.*?banner\"></a><br/>", "").replaceAll("powered.*?shrek.wang</a>", "");
        // 2、屏蔽buildFooter()函数，不构建广告
        String newCommonJs = commonJs.replace("this.buildFooter();", "// this.buildFooter();");

        // 创建filter进行过滤
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new RemoveAdFilter(newCommonJs));
        filterRegistrationBean.addUrlPatterns(commonJsPattern);
        return filterRegistrationBean;
    }

    @RequiredArgsConstructor
    private static class RemoveAdFilter implements Filter {
        private final String newCommonJs;

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            chain.doFilter(request, response);
            // 重置缓冲区，响应头不会被重置
            response.resetBuffer();
            // 写入新的CommonJs以响应
            response.getWriter().write(newCommonJs);
        }
    }
}
