package org.zero.component.spring.boot.jackson.conversion.datetime;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/1/5
 */
@JsonComponent
public class LocalDateTimeJsonComponent {
    public static class LocalDateTimeSerializer extends BaseDateTimeSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(LocalDateTimeUtil.format(value, format));
        }
    }

    public static class LocalDateTimeDeserializer extends BaseDateTimeDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            // 换回Jdk的LocalDateTime
            return Optional.ofNullable(preDeserialize(p, ctxt))
                    .map(DateTime::toLocalDateTime)
                    .orElse(null);
                    // .orElseThrow(() -> InvalidFormatException.from(p, ctxt.getContextualType(), "Deserialization to LocalDateTime failed"));
        }
    }
}
