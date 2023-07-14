package org.zero.component.mybatisplus.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.experimental.UtilityClass;
import org.zero.common.data.exception.UtilException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2023/1/5
 */
@UtilityClass
public class MpEnumUtil {
    /**
     * 方法缓存
     */
    private static final ConcurrentMap<Class<? extends Enum<?>>, Method> METHOD_MAP = new ConcurrentHashMap<>();

    public static Object invoke(Enum<?> enumObj) {
        Method method = MpEnumUtil.getMethod(enumObj.getDeclaringClass());
        return ReflectUtil.invoke(enumObj, method);
    }

    public static Method getMethod(Class<? extends Enum<?>> enumType) {
        return METHOD_MAP.computeIfAbsent(enumType, k -> {
            // 此处可使用自定义父类和注解，但因为Mp已经提供，所有无需重复造轮子
            if (IEnum.class.isAssignableFrom(k)) {
                return getMethodWithName(k, "getValue");
            } else {
                Field field = getAnnotatedField(k, EnumValue.class)
                        .orElseThrow(() -> new UtilException(String.format("Class[%s] fields could not find @EnumValue", k.getName())));
                return getMethodWithField(k, field);
            }
        });
    }

    public static Optional<Field> getAnnotatedField(Class<? extends Enum<?>> targetClass, Class<? extends Annotation> annotationClass) {
        return targetClass.isEnum() ? Arrays.stream(targetClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(annotationClass)).findFirst() : Optional.empty();
    }

    private static Method getMethodWithField(Class<?> clazz, Field field) {
        String fieldName = field.getName();
        String methodName = StrUtil.upperFirstAndAddPre(fieldName, "get");
        return getMethodWithName(clazz, methodName);
    }

    private static Method getMethodWithName(Class<?> clazz, String methodName) {
        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new UtilException(String.format("Class[%s] could not find %s()", clazz.getName(), methodName), e);
        }
    }
}
