package org.zero.component.alibaba.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2019/12/23
 */
@UtilityClass
public class TransmittableHolder {
    private static final TransmittableThreadLocal<ConcurrentMap<String, Object>> holder = TransmittableThreadLocal.withInitial(ConcurrentSkipListMap::new);

    public static void setVal(String key, Object val) {
        ConcurrentMap<String, Object> map = getMap();
        map.put(key, val);
    }

    public static Object getVal(String key) {
        ConcurrentMap<String, Object> map = getMap();
        return map.get(key);
    }


    public static <T> T getVal(String key, Class<T> type) {
        return type.cast(getVal(key));
    }

    public static void remove(String... keys) {
        if (Objects.isNull(keys) || keys.length == 0) {
            removeAll();
            return;
        }

        for (String key : keys) {
            removeOne(key);
        }
    }

    public static void removeOne(String key) {
        ConcurrentMap<String, Object> map = getMap();
        map.remove(key);
    }

    public static void removeAll() {
        ConcurrentMap<String, Object> map = getMap();
        map.clear();
    }

    private static ConcurrentMap<String, Object> getMap() {
        return holder.get();
    }
}
