package org.zero.demo.spring.boot.quartz.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.zero.demo.spring.boot.quartz.constant.JobType;
import org.zero.demo.spring.boot.quartz.context.JobContext;

import java.util.Map;

/**
 * @author zero
 * @since 2021/10/21
 */
@Slf4j
@UtilityClass
public class QuartzUtil extends org.zero.common.data.util.quartz.QuartzUtil {
    /**
     * 添加调度任务
     *
     * @param key          任务标识，请勿重复
     * @param jobType      任务类型，详情参见：{@link JobType}
     * @param cron         cron表达式
     * @param invokeTarget 任务执行类
     * @param invokeMethod 任务执行方法
     * @param param        任务
     */
    public static boolean scheduleJob(String key, JobType jobType, String cron, String invokeTarget, String invokeMethod, Map<String, Object> param) {
        return scheduleJob(key, jobType.getClazz(), createJobDataMap(invokeTarget, invokeMethod, param), cron);
    }

    public static boolean addJob(String key, JobType jobType, String invokeTarget, String invokeMethod, Map<String, Object> param) {
        return addJob(key, jobType.getClazz(), createJobDataMap(invokeTarget, invokeMethod, param));
    }

    public static boolean triggerJob(String key, String invokeTarget, String invokeMethod, Map<String, Object> param) {
        return triggerJob(key, createJobDataMap(invokeTarget, invokeMethod, param));
    }

    public static boolean addTriggerJob(String key, JobType jobType, String invokeTarget, String invokeMethod, Map<String, Object> param) {
        return addJob(key, jobType, invokeTarget, invokeMethod, param) && triggerJob(key);
    }

    public static boolean addTriggerJob(String key, JobType jobType, JobDataMap jobDataMap) {
        return addJob(key, jobType.getClazz(), jobDataMap) && triggerJob(key);
    }

    public static JobDataMap createJobDataMap(String invokeTarget, String invokeMethod, Map<String, Object> param) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JobContext.INVOKE_TARGET, invokeTarget);
        jobDataMap.put(JobContext.INVOKE_METHOD, invokeMethod);
        jobDataMap.put(JobContext.PARAM, param);
        return jobDataMap;
    }
}
