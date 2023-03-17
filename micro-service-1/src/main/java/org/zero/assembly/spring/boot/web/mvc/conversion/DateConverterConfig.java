package org.zero.assembly.spring.boot.web.mvc.conversion;

import cn.hutool.core.date.DateUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/6/4
 */
@Configuration(proxyBeanMethods = false)
public class DateConverterConfig {

    /**
     * Date转换器，用于转换RequestParam和PathVariable参数
     * 使用Hutool的日期解析工具类解析各种格式的日期格式
     */
    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public Converter<String, Date> dateConverter() {
        return DateUtil::parse;
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     * <p>
     * 因为FormattingConversionService将所有Converter添加进来的时候需要获取泛型信息，所以会抛出异常
     * <p>
     * 解决此问题方式1：使用注解@ConditionalOnBean(name = "requestMappingHandlerAdapter")
     * 解决此问题方式2：不使用Lambda表达式，老老实实使用匿名内部类
     */
    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return source -> DateUtil.parse(source).toLocalDateTime();
    }

    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     * <p>
     * 这种写法而不是LocalDateTimeUtil::parseDate，主要是为了DateUtil.parse()方法的多样处理
     */
    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public Converter<String, LocalDate> localDateConverter() {
        return source -> DateUtil.parse(source).toLocalDateTime().toLocalDate();
    }

    /**
     * LocalTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public Converter<String, LocalTime> localTimeConverter() {
        return source -> DateUtil.parse(source).toLocalDateTime().toLocalTime();
    }
}