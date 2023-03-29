package org.zero.component.spring.boot.web.mvc.conversion;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;

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

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            // 使用 hutool 工具集解析时间字符串，做到多格式兼顾
            return DateUtil.parse(p.getText()).toLocalDateTime();
        }
    }
}
