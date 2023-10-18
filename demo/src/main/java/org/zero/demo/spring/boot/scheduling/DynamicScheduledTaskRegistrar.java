package org.zero.demo.spring.boot.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.FixedDelayTask;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 目前只适用于计划任务（ScheduledTask），不能注册单次执行任务
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/3/16
 */
@Slf4j
@Component
public class DynamicScheduledTaskRegistrar extends ScheduledTaskRegistrar {
    private final Map<String, ScheduledTask> scheduledTaskMap = new LinkedHashMap<>(16);

    @Resource
    private TaskScheduler taskScheduler;
    // private ScheduledExecutorService scheduledExecutorService;

    /**
     * 新增cron任务
     */
    public synchronized boolean addCronTask(String taskId, Runnable task, String cron) {
        if (scheduledTaskMap.containsKey(taskId)) {
            return false;
        }
        CronTask cronTask = new CronTask(task, cron);
        ScheduledTask scheduledTask = this.scheduleCronTask(cronTask);
        scheduledTaskMap.put(taskId, scheduledTask);
        return true;
    }

    /**
     * 新增trigger任务
     */
    public synchronized boolean addTriggerTask(String taskId, Runnable task, Trigger trigger) {
        if (scheduledTaskMap.containsKey(taskId)) {
            return false;
        }
        TriggerTask triggerTask = new TriggerTask(task, trigger);
        ScheduledTask scheduledTask = this.scheduleTriggerTask(triggerTask);
        scheduledTaskMap.put(taskId, scheduledTask);
        return true;
    }

    /**
     * 新增fixed delay任务
     */
    public synchronized boolean addFixedDelayTask(String taskId, Runnable task, long delay, long interval) {
        if (scheduledTaskMap.containsKey(taskId)) {
            return false;
        }
        FixedDelayTask fixedDelayTask = new FixedDelayTask(task, interval, delay);
        ScheduledTask scheduledTask = this.scheduleFixedDelayTask(fixedDelayTask);
        scheduledTaskMap.put(taskId, scheduledTask);
        return true;
    }

    /**
     * 新增fixed rate任务
     */
    public synchronized boolean addFixedRateTask(String taskId, Runnable task, long delay, long interval) {
        if (scheduledTaskMap.containsKey(taskId)) {
            return false;
        }
        FixedRateTask fixedRateTask = new FixedRateTask(task, interval, delay);
        ScheduledTask scheduledTask = this.scheduleFixedRateTask(fixedRateTask);
        scheduledTaskMap.put(taskId, scheduledTask);
        return true;
    }

    /**
     * 取消任务
     */
    public synchronized void cancelCronTask(String taskId) {
        ScheduledTask scheduledTask = scheduledTaskMap.get(taskId);
        if (Objects.nonNull(scheduledTask)) {
            scheduledTask.cancel();
        }
        scheduledTaskMap.remove(taskId);
    }

    @Override
    public void afterPropertiesSet() {
        // this.setTaskScheduler(taskScheduler);
        this.setScheduler(taskScheduler);
        super.afterPropertiesSet();
    }

    @Override
    public void destroy() {
        scheduledTaskMap.values().forEach(ScheduledTask::cancel);
        super.destroy();
    }
}
