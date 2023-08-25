package org.zero.common.core.config.spring.boot.web.mvc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.PropertySource;
import org.zero.common.core.factory.YamlPropertySourceFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Zero
 * @since 2020/7/7
 */
@Setter
@Getter
@PropertySource(name = "sysWeb", value = "classpath:/web/web.yml", encoding = "UTF-8", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "sys.web")
public class WebMvcProperties {
    private InterceptorProperties intercept;
    private CorsProperties cors;
    private ResourceHandlerProperties resource;
    private ViewControllerProperties view;

    /**
     * yml配置：
     * <pre>
     * sys:
     *   web:
     *     intercept:
     *       enabled: true
     *       configs:
     *         "[org.zero.web.interceptor.CustomInterceptor]":
     *           intercepted-paths: "/**"
     *           excluded-paths: "/resource/**"
     * </pre>
     */
    @Setter
    @Getter
    public static class InterceptorProperties {
        /**
         * 默认排除的路径
         */
        protected static final String[] DEFAULT_EXCLUDE_PATHS = new String[]{
                "/**.html", "/**.htm",
                "/**.js", "/**.css",
                "/**.jpg", "/**.png", "/**.gif",
                "/html/**", "/js/**", "/css/**",
                "/img/**", "/images/**",
                "/resource/**", "/resources/**", "/res/**",
                "/asset/**", "/assets/**",
        };

        /**
         * 是否开启拦截器功能，默认：false
         */
        private boolean enabled = false;
        /**
         * 拦截器配置
         */
        private Map<String, Config> configs;

        @Setter
        @Getter
        public static class Config {
            /**
             * 拦截的路径，默认：/**
             */
            private String[] interceptedPaths = new String[]{"/**"};
            /**
             * 排除的路径，默认：{@link InterceptorProperties#DEFAULT_EXCLUDE_PATHS}
             */
            private String[] excludedPaths = DEFAULT_EXCLUDE_PATHS;
        }
    }

    /**
     * yml配置：
     * <pre>
     * sys:
     *   web:
     *     cors:
     *       enabled: true
     *       configs:
     *         "[/**]":
     *           allowed-origins: "*"
     *           allowed-headers: "*"
     *           allowed-methods: "*"
     *           exposed-headers: "*"
     *           allow-credentials: true
     *           max-age: 3600
     * </pre>
     */
    @Setter
    @Getter
    public static class CorsProperties {
        public static final String ALLOWED_ALL = "*";

        /**
         * 是否开启跨域，默认：false
         */
        private boolean enabled = false;
        /**
         * 跨域配置
         */
        private Map<String, Config> configs;

        @Setter
        @Getter
        public static class Config {
            /**
             * 缓存预检请求响应的时间，默认：30m
             */
            @DurationUnit(ChronoUnit.SECONDS)
            private Duration maxAge = Duration.ofMinutes(30);
            /**
             * 是否允许凭据，默认：true
             */
            private boolean allowCredentials = true;
            /**
             * 允许的源，默认：*
             */
            private String[] allowedOrigins = new String[]{ALLOWED_ALL};
            /**
             * 允许的请求头，默认：*
             */
            private String[] allowedHeaders = new String[]{ALLOWED_ALL};
            /**
             * 允许的请求方式，默认：*
             */
            private String[] allowedMethods = new String[]{ALLOWED_ALL};
            /**
             * 可以公开的请求头，默认：*
             */
            private String[] exposedHeaders = new String[]{ALLOWED_ALL};
        }
    }

    /**
     * yml配置：
     * <pre>
     * sys:
     *   web:
     *     resource:
     *       enabled: true
     *       configs:
     *         "[/sys/upload/**]":
     *           # 结尾必须加"/"，不然映射不到
     *           locations:
     *             - "file:///C:/Users/Zero/Data/Upload/"
     *             - "file:/home/zero/data/upload/"
     *           cache-max-age: 12
     *           time-unit: HOURS
     * </pre>
     */
    @Setter
    @Getter
    public static class ResourceHandlerProperties {
        /**
         * 默认本地资源位置
         */
        protected static final String[] DEFAULT_LOCATIONS = new String[]{
                "classpath:/static/", "classpath:/public/",
        };

        /**
         * 是否开启本地资源映射，默认：false
         */
        private boolean enabled = false;
        /**
         * 资源映射配置
         */
        private Map<String, Config> configs;

        @Setter
        @Getter
        public static class Config {
            /**
             * 最大缓存时间，如果为负数表示不缓存，默认：1
             */
            private long cacheMaxAge = 1L;

            /**
             * 时间单位，默认：h
             */
            private TimeUnit timeUnit = TimeUnit.HOURS;

            /**
             * 资源位置，默认：{@link ResourceHandlerProperties#DEFAULT_LOCATIONS}
             */
            private String[] locations = DEFAULT_LOCATIONS;
        }
    }

    /**
     * yml配置：
     * <pre>
     * sys:
     *   web:
     *     view:
     *       enabled: true
     *       configs:
     *         - type: view
     *           src: "/"
     *           dest: index
     *         - type: redirect
     *           src: "/test"
     *           dest: "/api/test"
     *         - type: status
     *           src: 404
     *           dest: "/no-found"
     * </pre>
     */
    @Setter
    @Getter
    public static class ViewControllerProperties {
        /**
         * 是否开启url映射，默认：false
         */
        private boolean enabled = false;
        /**
         * url映射配置
         */
        private List<Config> configs;

        @Setter
        @Getter
        @ToString
        public static class Config {
            /**
             * 映射类型，默认：简单视图映射
             */
            private MappingType type = MappingType.VIEW;
            /**
             * 原始目标
             */
            private String src;

            /**
             * 映射目标
             */
            private String dest;
        }

        public enum MappingType {
            /**
             * 简单视图映射
             */
            VIEW,
            /**
             * 重定向映射
             */
            REDIRECT,
            /**
             * Http状态码映射
             */
            STATUS,
            ;
        }
    }
}
