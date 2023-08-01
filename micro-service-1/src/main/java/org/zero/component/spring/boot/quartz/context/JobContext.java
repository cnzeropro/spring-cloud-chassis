package org.zero.component.spring.boot.quartz.context;

import lombok.experimental.UtilityClass;

/**
 * @author zero
 * @since 2023/8/1
 */
@UtilityClass
public class JobContext {
    private static final ThreadLocal<JobInfo> jobHolder = new InheritableThreadLocal<>();

    public static void set(JobInfo jobInfo) {
        jobHolder.set(jobInfo);
    }

    public static JobInfo get() {
        return jobHolder.get();
    }

    public static void remove() {
        jobHolder.remove();
    }
}
