package org.zero.common.core.config.spring.boot.scheduling;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 自动装配：
 * {@link TaskExecutionAutoConfiguration}
 * {@link TaskSchedulingAutoConfiguration}
 */
@AutoConfigureAfter({TaskExecutionAutoConfiguration.class, TaskSchedulingAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(TaskExecutorBuilder builder) {
        return builder.build();
    }

    @Bean
    protected ThreadPoolTaskScheduler threadPoolTaskScheduler(TaskSchedulerBuilder builder) {
        return builder.build();
    }
}
