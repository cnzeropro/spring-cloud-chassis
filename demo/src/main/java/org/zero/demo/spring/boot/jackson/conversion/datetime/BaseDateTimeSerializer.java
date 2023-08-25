package org.zero.demo.spring.boot.jackson.conversion.datetime;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
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
        // 如果不是日期时间类型，抛出异常
        if (!javaType.isTypeOrSubTypeOf(TemporalAccessor.class) || !javaType.isTypeOrSubTypeOf(Date.class)) {
            // 查找存在的序列化器
            // return prov.findValueSerializer(javaType, property);
            throw JsonMappingException.from(prov, String.format("Invalid type: %s", javaType));
        }

        BaseDateTimeSerializer<?> baseDateTimeSerializer = this;
        // 如果是日期类型，指定格式为"yyyy-MM-dd"
        if (javaType.isTypeOrSubTypeOf(ChronoLocalDate.class)) {
            baseDateTimeSerializer.format = DatePattern.NORM_DATE_PATTERN;
        }
        // 如果是时间类型，指定格式为"HH:mm:ss"
        if (javaType.isTypeOrSubTypeOf(LocalTime.class)) {
            baseDateTimeSerializer.format = DatePattern.NORM_TIME_PATTERN;
        }

        // 使用@JsonFormat指定的日期时间格式
        JsonFormat jsonFormat = property.getAnnotation(JsonFormat.class);
        if (Objects.nonNull(jsonFormat)) {
            String pattern = jsonFormat.pattern();
            if (StrUtil.isNotBlank(pattern)) {
                baseDateTimeSerializer.format = pattern;
            }
        }
        return baseDateTimeSerializer;
    }
}
