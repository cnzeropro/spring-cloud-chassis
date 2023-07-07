package org.zero.component.spring.boot.jackson.conversion.datetime;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/1/5
 */
@Slf4j
@JsonComponent
public class DateJsonComponent {
    public static class DateSerializer extends BaseDateTimeSerializer<Date> {
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(DateUtil.format(value, format));
        }
    }

    public static class DateDeserializer extends BaseDateTimeDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            // 换回Jdk的Date
            return Optional.ofNullable(preDeserialize(p, ctxt))
                    .map(DateTime::toJdkDate)
                    .orElse(null);
                    // .orElseThrow(() -> InvalidFormatException.from(p, ctxt.getContextualType(), "Deserialization to Date failed"));
        }
    }
}
