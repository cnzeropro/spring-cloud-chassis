package org.zero.common.data.util;

import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * @author zero
 * @since 2023/7/17
 */
@UtilityClass
public class ClassUtil {
    /**
     * 是否是指定包下的类
     */
    public static boolean isSpecifiedClass(Class<?> clazz, String... packageNames) {
        if (Objects.isNull(packageNames)) {
            return false;
        }

        final Package objectPackage = clazz.getPackage();
        if (null == objectPackage) {
            return false;
        }

        final String objectPackageName = objectPackage.getName();
        for (String packageName : packageNames) {
            if (objectPackageName.startsWith(packageName)) {
                return true;
            }
        }

        return false;
    }
}
