package org.zero.common.core.config.spring.boot.web.mvc;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author zero
 * @since 2018/3/21
 */
@EnableConfigurationProperties({WebMvcProperties.class})
@Configuration(proxyBeanMethods = false)
public class CorsConfig {
    @Resource
    private WebMvcProperties webMvcProperties;

    /**
     * 个人更倾向于使用以下注入方式，所以该方法不用于注入，仅展示
     */
    // @Bean
    CorsFilter corsFilter() {
        CorsConfigurationSource corsConfigurationSource = corsConfigurationSource();
        return new CorsFilter(corsConfigurationSource);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        WebMvcProperties.CorsProperties corsProperties = webMvcProperties.getCors();
        if (Objects.nonNull(corsProperties) && corsProperties.isEnabled()) {
            Map<String, WebMvcProperties.CorsProperties.Config> configs = corsProperties.getConfigs();
            if (!CollectionUtils.isEmpty(configs)) {
                configs.forEach((path, config) -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(Arrays.asList(config.getAllowedOrigins()));
                    corsConfiguration.setAllowedMethods(Arrays.asList(config.getAllowedMethods()));
                    corsConfigurationSource.registerCorsConfiguration(path, corsConfiguration);
                });
            }
        }
        return corsConfigurationSource;
    }
}
