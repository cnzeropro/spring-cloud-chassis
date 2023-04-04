package org.zero.component.spring.boot.scheduling;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Configuration(proxyBeanMethods = false)
@EnableScheduling
@EnableAsync
public class SchedulingConfig {
    /**
     * 异步方法注解@Async默认使用名称为taskExecutor的Executor执行任务
     * 详情请参见：{@link org.springframework.aop.interceptor.AsyncExecutionAspectSupport#DEFAULT_TASK_EXECUTOR_BEAN_NAME}，
     * {@link AsyncAnnotationBeanPostProcessor#DEFAULT_TASK_EXECUTOR_BEAN_NAME}
     */
    @Bean(name = AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME, destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        // 创建线程池对象
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // 线程池维护线程的最大数量，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(100);
        // 缓存队列容量
        taskExecutor.setQueueCapacity(50);
        // 线程的空闲时间，当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(200);
        // 异步方法内部线程名称
        taskExecutor.setThreadNamePrefix("AsyncTask-");
        /*
          当线程池的任务缓存队列已满，并且线程池中的线程数目达到最大线程数，如果还有任务到来就会采取任务拒绝策略。
          通常有以下四种策略:
          ThreadPoolExecutor.AbortPolicy：丢弃任务并抛出RejectedExecutionException异常
          ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常
          ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
          ThreadPoolExecutor.CallerRunsPolicy：重试添加当前的任务，自动重复调用execute()方法，直到成功
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        // 如果要支持阿里TTL，建议使用Java Agent来修饰JDK线程池实现类
        // 详情参见：com.alibaba.ttl.threadpool.agent.TtlAgent
//        Executor executor = TtlExecutors.getTtlExecutor(taskExecutor);
        return taskExecutor;
    }
}
