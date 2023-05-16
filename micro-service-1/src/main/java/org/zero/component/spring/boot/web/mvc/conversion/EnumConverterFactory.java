package org.zero.component.spring.boot.web.mvc.conversion;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.zero.component.mybatisplus.util.MpEnumUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 枚举转换类，使用时注意注册：
 * org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addFormatters(org.springframework.format.FormatterRegistry)
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/12/26
 */
@Slf4j
public class EnumConverterFactory implements ConverterFactory<String, Enum<?>> {
    private static final ConcurrentMap<Class<?>, Converter<String, ? extends Enum<?>>> CONVERTER_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<?>> Converter<String, E> getConverter(Class<E> targetType) {
        Converter<String, E> converter = (Converter<String, E>) CONVERTER_MAP.get(targetType);
        if (Objects.isNull(converter)) {
            converter = new Str2EnumConverter<>(targetType);
            CONVERTER_MAP.put(targetType, converter);
        }
        return converter;
    }

    static class Str2EnumConverter<E extends Enum<?>> implements Converter<String, E> {
        private final Map<String, E> enumMap = new HashMap<>();

        Str2EnumConverter(Class<E> enumType) {
            Method method = MpEnumUtil.getMethod(enumType);
            E[] enums = enumType.getEnumConstants();
            for (E e : enums) {
                try {
                    enumMap.put(Objects.toString(method.invoke(e)), e);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    log.warn("Get enum value error", ex);
                }
            }
        }

        @Override
        public E convert(String source) {
            E result = enumMap.get(source);
            if (Objects.isNull(result)) {
                throw new IllegalArgumentException(String.format("No enum matches [%s]", source));
            }
            return result;
        }
    }
}
