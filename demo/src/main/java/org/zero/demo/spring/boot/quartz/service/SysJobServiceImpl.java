package org.zero.demo.spring.boot.quartz.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zero.demo.spring.boot.quartz.constant.JobStatus;
import org.zero.demo.spring.boot.quartz.constant.JobType;
import org.zero.demo.spring.boot.quartz.model.SysJob;
import org.zero.demo.spring.boot.quartz.model.SysJobGroup;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zero
 * @since 2022/7/24
 */
@Slf4j
@Service
public class SysJobServiceImpl implements SysJobService {
    private static final String JOB_SUFFIX = "_job";
    private static final String TRIGGER_SUFFIX = "_trigger";

    @Resource
    private Scheduler scheduler;

    /**
     * todo: 应用启动时主动添加任务到Quartz
     */
    @PostConstruct
    public void initTask() {
    }

    public List<SysJob> listEnabled() {
        // todo: 查询数据库获取所有启用的任务
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(SysJob sysJob) {
        Long groupId = sysJob.getGroup();
        // todo: 查询数据库获取任务组

        SysJobGroup sysJobGroup = SysJobGroup.builder().id(groupId).build();
        // todo: 保存任务到数据库

        if (JobStatus.ENABLE.getStatus().equals(sysJob.getStatus())) {
            addScheduleJob(sysJob, sysJobGroup);
        }

        return true;
    }

    private boolean addScheduleJob(SysJob sysJob, SysJobGroup sysJobGroup) {
        try {
            Class<? extends Job> jobClass = JobType.getJobClass(sysJob.getType());
            // 构建job
            JobKey jobKey = new JobKey(sysJob.getKey() + JOB_SUFFIX, sysJobGroup.getKey() + JOB_SUFFIX);
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .withDescription(sysJob.getDescription())
                    .usingJobData("invokeTarget", sysJob.getInvokeTarget())
                    .usingJobData("invokeMethod", sysJob.getInvokeMethod())
                    .usingJobData("parma", sysJob.getParma())
                    // 调度器执行任务时，遇到'recovery'或者'fail-over'重新执行
                    .requestRecovery()
                    // 没有触发器也不删除该任务
                    // .storeDurably()
                    .build();
            // 构建trigger
            TriggerKey triggerKey = new TriggerKey(sysJob.getKey() + TRIGGER_SUFFIX, sysJobGroup.getKey() + TRIGGER_SUFFIX);
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    // .forJob(jobKey)
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(sysJob.getExpression()))
                    .startNow()
                    .build();
            // 开始任务调度
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (Exception e) {
            log.warn("Error in scheduling job", e);
            return false;
        }
    }
}
