package org.zero.demo.spring.boot.jackson.conversion.datetime;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.TemporalAccessorUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/1/5
 */
@JsonComponent
public class LocalTimeJsonComponent {
    public static class LocalTimeSerializer extends BaseDateTimeSerializer<LocalTime> {
        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(TemporalAccessorUtil.format(value, format));
        }
    }

    public static class LocalTimeDeserializer extends BaseDateTimeDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            // 换回Jdk的LocalTime
            return Optional.ofNullable(preDeserialize(p, ctxt))
                    .map(DateTime::toLocalDateTime)
                    .map(LocalDateTime::toLocalTime)
                    .orElse(null);
                    // .orElseThrow(() -> InvalidFormatException.from(p, ctxt.getContextualType(), "Deserialization to LocalTime failed"));
        }
    }
}
