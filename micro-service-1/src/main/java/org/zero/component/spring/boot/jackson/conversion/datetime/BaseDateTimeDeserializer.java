package org.zero.component.spring.boot.jackson.conversion.datetime;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Objects;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/1/5
 */
@Slf4j
public abstract class BaseDateTimeDeserializer<T> extends JsonDeserializer<T> implements ContextualDeserializer {
    protected String format = DatePattern.NORM_DATETIME_PATTERN;

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        JavaType javaType = property.getType();
        // 如果不是日期时间类型，抛出异常
        if (!javaType.isTypeOrSubTypeOf(TemporalAccessor.class) || !javaType.isTypeOrSubTypeOf(Date.class)) {
            // 查找存在的反序列化器
            // return ctxt.findRootValueDeserializer(javaType);
            throw JsonMappingException.from(ctxt, String.format("Invalid type: %s", javaType));
        }

        BaseDateTimeDeserializer<?> baseDateTimeDeserializer = this;
        // 使用@JsonFormat指定的日期时间格式
        JsonFormat jsonFormat = property.getAnnotation(JsonFormat.class);
        if (Objects.nonNull(jsonFormat)) {
            String pattern = jsonFormat.pattern();
            if (StrUtil.isNotBlank(pattern)) {
                baseDateTimeDeserializer.format = pattern;
            }
        }
        return baseDateTimeDeserializer;
    }

    protected DateTime preDeserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String text = p.getText();
        if (StrUtil.isEmpty(text)) {
            return null;
        }

        DateTime dateTime = null;
        try {
            dateTime = DateUtil.parse(text, format);
        } catch (Exception e) {
            log.warn(String.format("Could not parse date[%s] with format[%s]", text, format), e);
        }
        // 如果解析失败，再次尝试，此处做到多格式兼顾
        if (Objects.isNull(dateTime)) {
            dateTime = DateUtil.parse(text);
        }
        return dateTime;
    }
}
