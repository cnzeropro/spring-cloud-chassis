package org.zero.common.core.config.spring.cloud.openfegin;

import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

/**
 * 官网：<a href="https://spring.io/projects/spring-cloud-openfeign">Spring Cloud OpenFeign</a>
 * 自动装配：{@link org.springframework.cloud.openfeign.FeignAutoConfiguration}
 *
 * @author zero
 * @since 2021/2/14
 */
@EnableFeignClients(basePackages = {"org.zero.**.feign"})
@Configuration(proxyBeanMethods = false)
public class FeignConfig {
    /**
     * 用于@SpringQueryMap注解：
     * bean解析编码默认使用{@link feign.querymap.FieldQueryMapEncoder}，另外还有{@link feign.querymap.BeanQueryMapEncoder}，与FieldQueryMapEncoder不同的是：前者使用属性获取值，后者使用属性方法获取值（Getter）。
     * 但是两者会产生一些问题（比如无法使用复合对象）
     */
    @Bean
    public Feign.Builder feignBuilder() {
        // todo：使用自定义的QueryMapEncoder
        return Feign.builder().queryMapEncoder(/*new CustomQueryMapEncoder()*/null);
    }

    /**
     * 设置请求重试，默认：{@link Retryer.NEVER_RETRY}
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(200, TimeUnit.SECONDS.toMillis(1), 3);
    }

    /**
     * 设置请求超时时间
     */
    @Bean
    Request.Options feignOptions() {
        return new Request.Options(5, TimeUnit.SECONDS, 15, TimeUnit.SECONDS, true);
    }

    /**
     * 设置打印日志级别
     * <p>
     * NONE：不记录任何信息（默认）
     * BASIE：仅记录请求方法，URL以及响应状态码和执行时间
     * HEADERS：除了记录BASIE级别的信息之外，还会记录请求和响应得头信息
     * FULL：记录所有请求与响应得明细，包括头信息，请求体，元数据等
     */
    @Bean
    @Profile({"dev"})
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
