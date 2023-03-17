package org.zero.assembly.spring.cloud.openfegin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import feign.QueryMapEncoder;
import feign.codec.EncodeException;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.Field;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 使用方式：
 * <pre>
 *    @Bean
 *    public Feign.Builder feignBuilder() {
 * 		return Feign.builder().queryMapEncoder(new CustomQueryMapEncoder());
 *    }
 * </pre>
 *
 * @author zero
 * @since 2021/2/14
 */
public class CustomQueryMapEncoder implements QueryMapEncoder {
    /**
     * todo: bean解析完成
     *
     * @param object the object to encode
     * @return
     * @throws EncodeException
     */
    @Override
    public Map<String, Object> encode(Object object) throws EncodeException {
        return encode("", object, MapUtil.newHashMap(true));
    }

    private Map<String, Object> encode(String prefix, Object object, Map<String, Object> map) {
        if (BeanUtil.isBean(object.getClass())) {
            Field[] fields = ReflectUtil.getFields(object.getClass());
            for (Field field : fields) {
                encode(prefix, object, field, map);
            }
        } else {
            map.put(prefix, Objects.toString(object));
        }

        return map;
    }

    private Map<String, Object> encode(String prefix, Object object, Field field, Map<String, Object> map) {
        String key = StrUtil.isBlank(prefix) ? field.getName() : StrUtil.format("{}.{}", prefix, field.getName());

        Object fieldValue = ReflectUtil.getFieldValue(object, field);

        if (Objects.isNull(fieldValue)) {
            map.put(key, null);
            return map;
        }

        if (fieldValue instanceof Date) {
            String pattern = Optional.ofNullable(field.getAnnotation(DateTimeFormat.class))
                    .map(DateTimeFormat::pattern)
                    .orElse(DatePattern.NORM_DATETIME_PATTERN);
            map.put(key, DateUtil.format((Date) fieldValue, pattern));
            return map;
        }

        if (fieldValue instanceof TemporalAccessor) {
            String pattern = Optional.ofNullable(field.getAnnotation(DateTimeFormat.class))
                    .map(DateTimeFormat::pattern)
                    .orElse(DatePattern.NORM_DATETIME_PATTERN);
            map.put(key, TemporalAccessorUtil.format((TemporalAccessor) fieldValue, pattern));
            return map;
        }

        if (BeanUtil.isBean(fieldValue.getClass())) {
            encode(key, fieldValue, map);
            return map;
        }

        if (fieldValue instanceof Collection) {
            ((Collection) fieldValue).forEach(i -> encode(key, i, map));
            return map;
        }

        map.put(key, fieldValue.toString());

        return map;
    }
}
