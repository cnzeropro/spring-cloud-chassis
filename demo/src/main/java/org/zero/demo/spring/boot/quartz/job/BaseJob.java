package org.zero.demo.spring.boot.quartz.job;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.lang.Nullable;
import org.zero.demo.spring.boot.quartz.context.JobContext;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Objects;

/**
 * @author zero
 */
@Slf4j
public abstract class BaseJob implements Job {
    private static final ThreadLocal<Long> timeHolder = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            before(context);
            doExecute(context);
            after(context, null);
        } catch (Exception e) {
            after(context, e);
        }
    }

    /**
     * 任务执行前
     */
    protected void before(JobExecutionContext context) {
        if (log.isDebugEnabled()) {
            log.debug("job start");
        }
        timeHolder.set(System.nanoTime());
        JobContext.set(context);
    }

    /**
     * 任务执行
     */
    protected void doExecute(JobExecutionContext context) throws Exception {
        // 目标调用类
        Class<?> invokeTarget = ClassUtil.loadClass(JobContext.getTarget());
        // 目标调用类方法
        Method invokeMethod = ReflectUtil.getMethod(invokeTarget, JobContext.getMethod());
        ReflectUtil.invoke(ReflectUtil.newInstance(invokeTarget), invokeMethod);
    }

    /**
     * 任务执行后
     */
    protected void after(JobExecutionContext context, @Nullable Exception exception) {
        JobContext.remove();
        long endTime = System.nanoTime();
        Long startTime = timeHolder.get();
        timeHolder.remove();

        if (Objects.isNull(exception)) {
            if (log.isDebugEnabled()) {
                log.debug("job executed successfully.");
            }
        } else {
            log.warn("job executed fail.", exception);
        }

        if (log.isDebugEnabled()) {
            log.debug("job end, time: {}", Duration.ofNanos(endTime - startTime));
        }
    }
}
