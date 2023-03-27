package org.zero.assembly.spring.boot.web.mvc;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Zero
 */
@Slf4j
@Configuration
@EnableWebMvc
@EnableConfigurationProperties({WebMvcProperties.class})
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${spring.mvc.async.request-timeout:30000}")
    private long requestTimeout;

    @Resource
    private WebMvcProperties webMvcProperties;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcProperties.InterceptorProperties interceptProperties = webMvcProperties.getIntercept();
        if (Objects.nonNull(interceptProperties) && interceptProperties.isEnabled()) {
            Map<String, WebMvcProperties.InterceptorProperties.Config> configs = interceptProperties.getConfigs();
            if (CollUtil.isNotEmpty(configs)) {
                configs.forEach((clazz, config) -> {
                    try {
                        HandlerInterceptor interceptor = ReflectUtil.newInstance(clazz);
                        registry.addInterceptor(interceptor)
                                .addPathPatterns(config.getInterceptedPaths())
                                .excludePathPatterns(config.getExcludedPaths());
                    } catch (Exception e) {
                        log.warn(String.format("Add HandlerInterceptor error: %s", clazz), e);
                    }
                });
            }
        }
    }

    /**
     * 添加跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcProperties.CorsProperties corsProperties = webMvcProperties.getCors();
        if (Objects.nonNull(corsProperties) && corsProperties.isEnabled()) {
            Map<String, WebMvcProperties.CorsProperties.Config> configs = corsProperties.getConfigs();
            if (CollUtil.isNotEmpty(configs)) {
                configs.forEach((path, config) -> registry.addMapping(path)
                        .allowedOriginPatterns(config.getAllowedOrigins())
                        .allowedMethods(config.getAllowedMethods())
                        .allowedHeaders(config.getAllowedHeaders())
                        .allowCredentials(config.isAllowCredentials())
                        .exposedHeaders(config.getExposedHeaders())
                        .maxAge(config.getMaxAge()));
            }
        }
    }

    /**
     * 添加本地资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcProperties.ResourceHandlerProperties resourceHandlerProperties = webMvcProperties.getResource();
        if (Objects.nonNull(resourceHandlerProperties) && resourceHandlerProperties.isEnabled()) {
            Map<String, WebMvcProperties.ResourceHandlerProperties.Config> configs = resourceHandlerProperties.getConfigs();
            if (CollUtil.isNotEmpty(configs)) {
                configs.forEach((path, config) -> {
                    ResourceHandlerRegistration registration = registry.addResourceHandler(path)
                            .addResourceLocations(config.getLocations());
                    if (config.getCacheMaxAge() < 0) {
                        registration.setCacheControl(CacheControl.noCache());
                    } else {
                        registration.setCacheControl(CacheControl.maxAge(config.getCacheMaxAge(), TimeUnit.HOURS));
                    }
                });
            }
        }
    }

    /**
     * 添加视图控制器映射
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcProperties.ViewControllerProperties viewControllerProperties = webMvcProperties.getView();
        if (Objects.nonNull(viewControllerProperties) && viewControllerProperties.isEnabled()) {
            List<WebMvcProperties.ViewControllerProperties.Config> configs = viewControllerProperties.getConfigs();
            if (CollUtil.isNotEmpty(configs)) {
                configs.forEach(config -> {
                    try {
                        if (WebMvcProperties.ViewControllerProperties.MappingType.View.equals(config.getType())) {
                            registry.addViewController(config.getSrc()).setViewName(config.getDest());
                        } else if (WebMvcProperties.ViewControllerProperties.MappingType.Redirect.equals(config.getType())) {
                            registry.addRedirectViewController(config.getSrc(), config.getDest());
                        } else if (WebMvcProperties.ViewControllerProperties.MappingType.Status.equals(config.getType())) {
                            registry.addStatusController(config.getSrc(), HttpStatus.valueOf(Integer.parseInt(config.getDest())));
                        }
                    } catch (Exception e) {
                        log.warn(String.format("Add ViewController error: %s", config), e);
                    }
                });
            }
        }
    }

    /**
     * 添加参数解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new CustomArgumentResolver());
    }

    /**
     * 添加格式化器和转换器
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatter(new CustomFormatter());
//        registry.addConverter(new CustomConverter());
//        registry.addConverterFactory(new CustomConverterFactory());
    }

    /**
     * 添加返回值处理器
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
//        handlers.add(new CustomReturnValueHandler());
    }

    /**
     * 配置路径参数匹配配置
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        // 设置不移除分号“;”后面的内容，矩阵变量功能就可以生效
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    /**
     * 配置异步请求支持
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.registerCallableInterceptors(new TimeoutCallableProcessingInterceptor())
                .setTaskExecutor(asyncRequestTaskExecutor())
                // 请求超时时间，默认30s
                .setDefaultTimeout(requestTimeout);
    }

    /**
     * 扩展Http消息转换器
     * <p>
     * 配置Http消息转换器最好不要重写configureMessageConverters方法，而是重写该方法
     * 因为前者会关闭默认转换器注册
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new CustomMessageConverter());
    }

    /**
     * 扩展异常解析器
     */
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
//        resolvers.add(new CustomHandlerExceptionResolvers());
    }

    /**
     * spring异步请求默认使用SimpleAsyncTaskExecutor，此处重新配置异步请求的线程池
     */
    private ThreadPoolTaskExecutor asyncRequestTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(25);
        taskExecutor.setKeepAliveSeconds(200);
        taskExecutor.setThreadNamePrefix("AsyncRequestTask-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
