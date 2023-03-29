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
import java.time.LocalDate;

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

    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            // 使用 hutool 工具集解析时间字符串，做到多格式兼顾
            return DateUtil.parse(p.getText()).toLocalDateTime().toLocalDate();
        }
    }
}
