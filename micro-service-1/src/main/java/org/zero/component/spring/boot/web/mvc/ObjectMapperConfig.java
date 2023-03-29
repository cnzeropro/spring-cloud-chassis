package org.zero.component.spring.boot.web.mvc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author zero
 * @since 2023/2/20
 */
@Configuration(proxyBeanMethods = false)
public class ObjectMapperConfig {
    @Bean
    @Primary
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // Include.ALWAYS：序列化对象的所有属性（默认）
        // Include.NON_NULL：只有不为null的字段才被序列化，属性为NULL不序列化
        // Include.NON_EMPTY：如果为null或者空字符串和空集合都不会被序列化
        // Include.NON_DEFAULT：属性为默认值不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化时遇到空对象不抛出异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 反序列化时遇到不知名属性不抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 禁止序列化日期为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 禁止反序列化时上下文的TimeZone覆盖其他时区信息
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        // 转换为格式化的json（控制台打印时可以自动格式化规范）
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 指定可见属性（ALL表示所有）以及其访问修饰符范围（ANY表示所有）
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，比如String，Integer等会抛出异常
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return objectMapper;
    }
}
