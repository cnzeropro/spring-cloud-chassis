package org.zero.demo.spring.boot.web.mvc.conversion;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.zero.common.data.util.mybatisplus.MpEnumUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 字符串到枚举的转换工厂类，使用时请注册到容器：
 * {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addFormatters(org.springframework.format.FormatterRegistry)}
 * <p>
 * 另外，spring自带的转换器工厂类参考：{@link org.springframework.core.convert.support.StringToEnumConverterFactory}
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/12/26
 */
@Slf4j
public class StrToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {
    @SuppressWarnings("rawtypes")
    private static final ConcurrentMap<Class, Converter> CONVERTER_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<?>> Converter<String, E> getConverter(Class<E> targetType) {
        return CONVERTER_MAP.computeIfAbsent(targetType, Str2EnumConverter::new);
        // Converter<String, E> converter = (Converter<String, E>) CONVERTER_MAP.get(targetType);
        // if (Objects.isNull(converter)) {
        //     converter = new Str2EnumConverter<>(targetType);
        //     CONVERTER_MAP.put(targetType, converter);
        // }
        // return converter;
    }

    private static class Str2EnumConverter<E extends Enum<?>> implements Converter<String, E> {
        private final Map<String, E> enumMap = new HashMap<>();

        Str2EnumConverter(Class<E> enumType) {
            E[] enums = enumType.getEnumConstants();
            for (E e : enums) {
                Object invoke = MpEnumUtil.invoke(e);
                enumMap.put(Objects.toString(invoke), e);
            }
        }

        @Override
        public E convert(String source) {
            E result = enumMap.get(source);
            if (Objects.isNull(result) && log.isWarnEnabled()) {
                log.warn("No enum matches [{}]", source);
            }
            return result;
        }
    }
}
