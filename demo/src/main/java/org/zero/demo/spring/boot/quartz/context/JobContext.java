package org.zero.demo.spring.boot.quartz.context;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.Map;

/**
 * @author zero
 * @since 2023/8/1
 */
@UtilityClass
public class JobContext {
    private static final ThreadLocal<JobExecutionContext> jobHolder = new InheritableThreadLocal<>();

    public static void set(JobExecutionContext context) {
        jobHolder.set(context);
    }

    public static JobExecutionContext get() {
        return jobHolder.get();
    }

    public static JobDataMap getMergedJobDataMap() {
        JobExecutionContext context = get();
        return context.getMergedJobDataMap();
    }

    public static String getTarget() {
        JobDataMap jobDataMap = getMergedJobDataMap();
        return jobDataMap.getString("invokeTarget");
    }

    public static String getMethod() {
        JobDataMap jobDataMap = getMergedJobDataMap();
        return jobDataMap.getString("invokeMethod");
    }

    public static Map<String, Object> getParam() {
        JobDataMap jobDataMap = getMergedJobDataMap();
        return JSONUtil.toBean(jobDataMap.getString("param"), new TypeReference<Map<String, Object>>() {
        }, true);
    }

    public static void remove() {
        jobHolder.remove();
    }
}
