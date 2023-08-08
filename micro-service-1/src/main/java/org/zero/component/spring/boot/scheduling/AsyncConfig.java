package org.zero.component.spring.boot.scheduling;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import static org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME;

/**
 * 相关自动装配参见：
 * {@link org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration}
 * {@link org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration}
 */
@EnableAsync
@AutoConfigureAfter(TaskExecutionAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class AsyncConfig implements AsyncConfigurer {
    /**
     * 获取当前机器的核数
     */
    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    @Autowired(required = false)
    @Qualifier(DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 异步方法注解@Async默认使用名称为taskExecutor的Executor执行任务。
     * 详情请参见：
     * {@link org.springframework.aop.interceptor.AsyncExecutionAspectSupport#DEFAULT_TASK_EXECUTOR_BEAN_NAME}
     * {@link org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor#DEFAULT_TASK_EXECUTOR_BEAN_NAME}
     */
    @Override
    @Bean(DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public Executor getAsyncExecutor() {
        if (Objects.isNull(taskExecutor)) {
            taskExecutor = new ThreadPoolTaskExecutor();
        }
        taskExecutor.setCorePoolSize(CPU_NUM * 2);
        taskExecutor.setQueueCapacity(CPU_NUM * 10);
        taskExecutor.setThreadNamePrefix("async-task-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 如果要支持阿里TTL，建议使用Java Agent来修饰JDK线程池实现类，详情参见：com.alibaba.ttl.threadpool.agent.TtlAgent
        // 或者使用相关包装方法包装一下
        // Executor executor = TtlExecutors.getTtlExecutor(taskExecutor);
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
