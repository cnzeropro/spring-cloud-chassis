package org.zero.component.spring.boot.web.mvc.conversion;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.zero.component.mybatisplus.util.MpEnumUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 枚举json反序列化器，已使用@JsonComponent自动加入{@link ObjectMapper}
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2023/1/5
 */
@Slf4j
@JsonComponent
@NoArgsConstructor
@AllArgsConstructor
public class EnumDeserializer extends JsonDeserializer<Enum<?>> implements ContextualDeserializer {
    private Class<?> clazz;

    @Override
    public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String text = p.getText();
        if (Objects.isNull(clazz) || !clazz.isEnum()) {
            throw new IllegalArgumentException(String.format("Class[%s] is not enum class", clazz));
        }
        Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) clazz;
        Method method = MpEnumUtil.getMethod(enumType);
        Enum<?>[] enums = enumType.getEnumConstants();
        for (Enum<?> e : enums) {
            try {
                if (Objects.equals(Objects.toString(method.invoke(e)), text)) {
                    return e;
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                log.warn("Get enum value error", ex);
            }
        }
        throw new IllegalArgumentException(String.format("No enum matches [%s]", text));
    }

    @Override
    public JsonDeserializer<Enum<?>> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        Class<?> rawClass = ctxt.getContextualType().getRawClass();
        return new EnumDeserializer(rawClass);
    }
}
