package org.zero.demo.spring.boot.quartz.context;

import lombok.experimental.UtilityClass;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author zero
 * @since 2023/8/1
 */
@UtilityClass
public class JobContext {
    public static final String INVOKE_TARGET = "invokeTarget";
    public static final String INVOKE_METHOD = "invokeMethod";
    public static final String PARAM = "param";

    private static final ThreadLocal<JobExecutionContext> holder = new InheritableThreadLocal<>();

    public static void set(JobExecutionContext context) {
        holder.set(context);
    }

    public static JobExecutionContext get() {
        return holder.get();
    }

    public static JobDataMap getMergedJobDataMap() {
        JobExecutionContext context = get();
        return context.getMergedJobDataMap();
    }

    public static String getTarget() {
        JobDataMap jobDataMap = getMergedJobDataMap();
        return jobDataMap.getString(INVOKE_TARGET);
    }

    public static String getMethod() {
        JobDataMap jobDataMap = getMergedJobDataMap();
        return jobDataMap.getString(INVOKE_METHOD);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getParam() {
        JobDataMap jobDataMap = getMergedJobDataMap();
        return Optional.ofNullable(jobDataMap.get(PARAM))
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .orElseGet(HashMap::new);
    }

    public static Object getParam(String key) {
        Map<String, Object> param = getParam();
        return param.get(key);
    }

    public static Object getParam(String key, Object defaultVal) {
        Map<String, Object> param = getParam();
        return param.getOrDefault(key, defaultVal);
    }

    public static <T> T getParam(String key, Class<T> clazz) {
        return Optional.ofNullable(getParam(key))
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .orElse(null);
    }

    public static <T> T getParam(String key, Class<T> clazz, T defaultVal) {
        return Optional.ofNullable(getParam(key))
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .orElse(defaultVal);
    }

    public static void remove() {
        holder.remove();
    }
}
