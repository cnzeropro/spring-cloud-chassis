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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/1/5
 */
@JsonComponent
public class LocalDateJsonComponent {
    public static class LocalDateSerializer extends BaseDateTimeSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(LocalDateTimeUtil.format(value, format));
        }
    }

    public static class LocalDateDeserializer extends BaseDateTimeDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            // 换回Jdk的LocalDate
            return Optional.ofNullable(preDeserialize(p, ctxt))
                    .map(DateTime::toLocalDateTime)
                    .map(LocalDateTime::toLocalDate)
                    .orElse(null);
                    // .orElseThrow(() -> InvalidFormatException.from(p, ctxt.getContextualType(), "Deserialization to LocalDate failed"));
        }
    }
}
