package org.zero.component.spring.cloud.openfegin;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author zero
 * @since 2021/2/14
 */
@Configuration(proxyBeanMethods = false)
@EnableFeignClients(basePackages = {"org.zero.feign"})
public class FeignConfig {
    /**
     * 设置请求重试，默认：{@link Retryer.NEVER_RETRY}
     * 最好还是配置文件指定
     */
//    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(200, TimeUnit.SECONDS.toMillis(10), 3);
    }

    /**
     * 设置请求超时时间
     * 最好还是配置文件指定
     */
//    @Bean
    Request.Options feignOptions() {
        return new Request.Options(30, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true);
    }

    /**
     * 设置打印日志级别
     * <p>
     * NONE：不记录任何信息
     * BASIE：仅记录请求方法，URL以及响应状态码和执行时间
     * HEADERS：除了记录BASIE级别的信息之外，还会记录请求和响应得头信息
     * FULL：记录所有请求与响应得明细，包括头信息，请求体，元数据等
     */
    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

    /**
     * 支持对application/x-www-form-urlencoded、multipart/form-data格式的表单数据编码
     * SpringEncoder是Spring Cloud默认使用的Encoder，会调用Spring MVC中的消息转换器（HttpMessageConverter）进行编码，从而支持多种数据格式
     * 但其能力有限，无法编码数据流（文件），因此注入SpringFormEncoder
     */
    @Bean
    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    /**
     * 用于@SpringQueryMap注解
     * bean解析编码默认使用{@link feign.querymap.FieldQueryMapEncoder}，但是会产生一些问题（比如日期时间类的序列化痛点）
     * 另外还有{@link feign.querymap.BeanQueryMapEncoder}，与FieldQueryMapEncoder不同的是：前者使用属性获取值，后者使用属性方法获取值（Getter）
     */
    @Bean
    public Feign.Builder feignBuilder() {
        // 使用自定义的QueryMapEncoder，解决默认QueryMapEncoder的一些问题（比如日期时间类的序列化痛点）
        return Feign.builder().queryMapEncoder(new CustomQueryMapEncoder());
    }

    /**
     * 用于Spring MVC get请求的相关注解（@RequestParam，@PathVariable，...）
     * 注册转换器，虽然spring已经注入了比较完善的格式器，但是相关日期时间类还是存在问题
     * 因为feign默认使用FallbackObjectToStringConverter，其中直接调用的是对象的toString()方法，最终导致url参数异常
     *
     * @return
     */
    @Bean
    public FeignFormatterRegistrar feignFormatterRegistrar(FormatterRegistry r) {
        // 使用统一时间格式："yyyy-MM-dd HH:mm:ss"
        return registry -> {
            if (Objects.nonNull(r)) {
                registry = r;
            }
            // 此处请勿使用lambda表达式，保留泛型以便获取转换源信息
            registry.addConverter(new Converter<Date, String>() {
                @Override
                public String convert(Date source) {
                    return DateUtil.format(source, DatePattern.NORM_DATETIME_PATTERN);
                }
            });
            registry.addConverter(new Converter<TemporalAccessor, String>() {
                @Override
                public String convert(TemporalAccessor source) {
                    return TemporalAccessorUtil.format(source, DatePattern.NORM_DATETIME_PATTERN);
                }
            });
        };
    }
}
