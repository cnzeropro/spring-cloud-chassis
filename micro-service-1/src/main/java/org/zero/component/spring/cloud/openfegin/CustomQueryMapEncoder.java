package org.zero.component.spring.cloud.openfegin;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import feign.QueryMapEncoder;
import feign.codec.EncodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

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
@Slf4j
public class CustomQueryMapEncoder implements QueryMapEncoder {
    private static final String BEAN_BASE_PACKAGE = "org.zero.common.data.model";

    @Override
    public Map<String, Object> encode(Object object) {
        return encode("", object, MapUtil.newHashMap(true));
    }

    private Map<String, Object> encode(String prefix, Object object, Map<String, Object> map) {
        // 如果为空
        if (Objects.isNull(object)) {
            return map;
        }

        Class<?> clazz = object.getClass();

        // 如果是应用的bean
        if (org.zero.common.data.util.ClassUtil.isSpecifiedClass(clazz, BEAN_BASE_PACKAGE)) {
            Field[] fields = ReflectUtil.getFields(clazz);
            for (Field field : fields) {
                encodeField(prefix, object, field, map);
            }
            return map;
        }

        // 如果是map
        // 此处为什么没有直接返回objectMap，是因为考虑到map的value可能是其他类型，比如Bean，List，Map等等
        if (object instanceof Map) {
            Map<Object, Object> objectMap = (Map<Object, Object>) object;
            objectMap.forEach((k, v) -> {
                String key = StrUtil.isBlank(prefix) ? String.valueOf(k) : StrUtil.format("{}.{}", prefix, k);
                encode(key, v, map);
            });
            return map;
        }

        // 如果是首次进入该方法，则不应该出现下面的类型，所以抛出异常
        if (StrUtil.isBlank(prefix)) {
            log.warn("encode object fail: {}", object);
            throw new EncodeException("对象编码异常，不支持此类型：" + clazz);
        }

        // 如果是数组
        if (ArrayUtil.isArray(object)) {
            encodeArray(prefix, object, map);
            return map;
        }

        // 如果是可迭代的，如List
        if (object instanceof Iterable || object instanceof Iterator || object instanceof Enumeration) {
            Iterator<?> iterator;
            if (object instanceof Iterable) {
                Iterable<?> iterable = (Iterable<?>) object;
                iterator = iterable.iterator();
            } else if (object instanceof Iterator) {
                iterator = (Iterator<?>) object;
            } else {
                iterator = IterUtil.asIterator((Enumeration<?>) object);
            }
            // 为了公用一个方法，做了一点转换，有点性能浪费的感觉
            List<?> tempList = IterUtil.toList(iterator);
            Object[] objects = tempList.toArray();
            encodeArray(prefix, objects, map);
            return map;
        }

        // 日期时间类型
        if (object instanceof Date || object instanceof Calendar || object instanceof TemporalAccessor) {
            map.put(prefix, format(null, object));
            return map;
        }

        // 其他类型
        map.put(prefix, StrUtil.utf8Str(object));
        return map;
    }

    private Map<String, Object> encodeArray(String prefix, Object arrayObj, Map<String, Object> map) {
        Class<?> componentType = ArrayUtil.getComponentType(arrayObj);
        Object[] objects = ArrayUtil.cast(componentType, arrayObj);

        // 基本类型及其包装类
        if (ClassUtil.isBasicType(componentType) ||
                // 数字类型
                Number.class.isAssignableFrom(componentType) ||
                // 字符序列类型
                CharSequence.class.isAssignableFrom(componentType)) {
            String join = ArrayUtil.join(objects, ",");
            map.put(prefix, join);
            return map;
        }

        // 日期时间类型
        if (Date.class.isAssignableFrom(componentType) || Calendar.class.isAssignableFrom(componentType) || TemporalAccessor.class.isAssignableFrom(componentType)) {
            StringJoiner sj = new StringJoiner(",");
            for (Object obj : objects) {
                sj.add(format(null, obj));
            }
            map.put(prefix, sj.toString());
            return map;
        }

        // 其他类型，比如bean、map等等
        for (int i = 0; i < objects.length; i++) {
            String key = StrUtil.format("{}[{}]", prefix, i);
            encode(key, objects[i], map);
        }
        return map;
    }

    /**
     * bean中字段处理
     */
    private Map<String, Object> encodeField(String prefix, Object object, Field field, Map<String, Object> map) {
        String key = StrUtil.isBlank(prefix) ? field.getName() : StrUtil.format("{}.{}", prefix, field.getName());
        Object fieldValue = ReflectUtil.getFieldValue(object, field);

        // 空值
        if (Objects.isNull(fieldValue)) {
            // 不添加空值
//            map.put(key, null);
            return map;
        }

        // 日期时间类型
        if (fieldValue instanceof Date || fieldValue instanceof Calendar || fieldValue instanceof TemporalAccessor) {
            map.put(key, format(field, fieldValue));
            return map;
        }

        // 其他类型
        encode(key, fieldValue, map);
        return map;
    }

    /**
     * 日期时间类型格式化
     */
    private String format(Field field, Object fieldValue) {
        String pattern = Opt.ofNullable(field).map(f -> f.getAnnotation(DateTimeFormat.class)).map(DateTimeFormat::pattern)
                .or(() -> Opt.ofNullable(field).map(f -> f.getAnnotation(JsonFormat.class)).map(JsonFormat::pattern)
                        .or(() -> Opt.ofNullable(SpringUtil.getProperty("spring.mvc.format.date-time"))
                                .or(() -> Opt.ofNullable(SpringUtil.getProperty("spring.jackson.date-format")))))
                .orElse(DatePattern.NORM_DATETIME_PATTERN);

        if (fieldValue instanceof Date) {
            return DateUtil.format((Date) fieldValue, pattern);
        }

        if (fieldValue instanceof Calendar) {
            return DateUtil.format(((Calendar) fieldValue).getTime(), pattern);
        }

        if (fieldValue instanceof LocalDate) {
            if (StrUtil.containsAny(pattern, "HH", "mm", "ss")) {
                pattern = DatePattern.NORM_DATE_PATTERN;
            }
        }

        if (fieldValue instanceof LocalTime) {
            if (StrUtil.containsAny(pattern, "yyyy", "MM", "dd")) {
                pattern = DatePattern.NORM_TIME_PATTERN;
            }
        }

        if (fieldValue instanceof TemporalAccessor) {
            return TemporalAccessorUtil.format((TemporalAccessor) fieldValue, pattern);
        }

        throw new EncodeException("非日期时间类型，无法格式化");
    }
}
