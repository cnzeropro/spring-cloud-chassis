package org.zero.component.spring.boot.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 建议采用配置文件设置，无需自定义注入ObjectMapper
 * <p>
 * 自动装配参见：{@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration}
 *
 * @author zero
 * @since 2023/2/20
 */
@Configuration(proxyBeanMethods = false)
public class ObjectMapperConfig {
    @Bean
    @Primary
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 指定序列化时应该包含的属性
        // Include.ALWAYS：序列化对象的所有属性（默认）
        // Include.NON_NULL：只有不为null的字段才被序列化，属性为NULL不序列化
        // Include.NON_EMPTY：如果为null或者空字符串和空集合都不会被序列化
        // Include.NON_DEFAULT：属性为默认值不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 指定元素类型（ALL表示所有）以及其访问修饰符范围（ANY表示所有）
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定输入的类型，类必须是非final修饰的，比如String，Integer等会抛出异常
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // 指定命名策略——驼峰命名法
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);

        // 使用BigDecimal.toPlainString序列化，不输出科学计数法的值
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        // 转换为格式化的JSON（控制台打印时可以自动格式化规范）
//       objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 当JSON字符串为空时，反序列化对象为null
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        // 禁止序列化时遇到空对象抛出异常（序列化为空JSON串）
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 禁止序列化Date为时间戳（序列化为时间文本，具体以时间格式化为准）
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 禁止反序列化时遇到不知名属性抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 禁止反序列化时上下文的TimeZone覆盖其他时区信息
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        return objectMapper;
    }
}
