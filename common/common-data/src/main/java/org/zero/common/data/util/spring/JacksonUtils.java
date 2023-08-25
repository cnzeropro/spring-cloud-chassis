package org.zero.common.data.util.spring;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Type;

/**
 * @author zero
 * @since 2023/7/19
 */
@UtilityClass
public class JacksonUtils {
    private static final ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

    @SneakyThrows
    public static String toJsonStr(Object value) {
        return objectMapper.writeValueAsString(value);
    }

    @SneakyThrows
    public static <T> T toObj(String jsonStr, Type type) {
        return toObj(jsonStr, objectMapper.constructType(type));
    }

    @SneakyThrows
    public static <T> T toObj(String jsonStr, Class<T> clazz) {
        return objectMapper.readValue(jsonStr, clazz);
    }

    @SneakyThrows
    public static <T> T toObj(String jsonStr, TypeReference<T> typeReference) {
        return objectMapper.readValue(jsonStr, typeReference);
    }

    @SneakyThrows
    public static <T> T toObj(String jsonStr, JavaType javaType) {
        return objectMapper.readValue(jsonStr, javaType);
    }
}
