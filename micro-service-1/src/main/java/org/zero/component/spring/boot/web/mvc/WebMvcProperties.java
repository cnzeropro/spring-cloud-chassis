package org.zero.component.spring.boot.web.mvc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @author Zero
 * @since 2020/7/7
 */
@Setter
@Getter
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
        public static final String[] DEFAULT_EXCLUDE_PATHS = new String[]{
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
             * 排除的路径，默认：{@code DEFAULT_EXCLUDE_PATHS}
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
             * 缓存预检请求响应的时间，单位：秒（s），默认：30m
             */
            private long maxAge = 30 * 60L;
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
     *           cache-max-age: 3600
     * </pre>
     */
    @Setter
    @Getter
    public static class ResourceHandlerProperties {
        /**
         * 默认本地资源位置
         */
        public static final String[] DEFAULT_LOCATIONS = new String[]{
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
             * 最大缓存时间，如果为负数表示不缓存，单位：h，默认：1h
             */
            private long cacheMaxAge = 1L;
            /**
             * 资源位置，默认：{@code DEFAULT_LOCATIONS}
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
     *         - type: View
     *           src: "/"
     *           dest: index
     *         - type: Redirect
     *           src: "/test"
     *           dest: "/api/test"
     *         - type: Status
     *           src: "/no-found"
     *           dest: 404
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
            private MappingType type = MappingType.View;
            /**
             * 原始URL
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
            View,
            /**
             * 重定向映射
             */
            Redirect,
            /**
             * Http状态码映射
             */
            Status,
            ;
        }
    }
}
