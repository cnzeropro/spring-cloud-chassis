package org.zero.component.spring.boot.web.mvc.conversion;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Objects;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/1/5
 */
public abstract class BaseDateTimeSerializer<T> extends JsonSerializer<T> implements ContextualSerializer {
    protected String format = DatePattern.NORM_DATETIME_PATTERN;

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (Objects.isNull(property)) {
            return prov.getDefaultNullValueSerializer();
        }

        JavaType javaType = property.getType();
        if (!javaType.isTypeOrSuperTypeOf(TemporalAccessor.class) || !javaType.isTypeOrSuperTypeOf(Date.class)) {
            return prov.findValueSerializer(javaType, property);
        }

        BaseDateTimeSerializer<?> baseDateTimeSerializer = this;
        JsonFormat jsonFormat = property.getAnnotation(JsonFormat.class);
        if (Objects.nonNull(jsonFormat)) {
            baseDateTimeSerializer.format = jsonFormat.pattern();
        }
        return baseDateTimeSerializer;
    }
}
