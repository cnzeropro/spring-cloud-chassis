package org.zero.demo.spring.boot.quartz.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zero.demo.spring.boot.quartz.constant.JobStatus;
import org.zero.demo.spring.boot.quartz.constant.JobType;
import org.zero.demo.spring.boot.quartz.model.SysJob;
import org.zero.demo.spring.boot.quartz.util.QuartzUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<SysJob> listEnabled() {
        // todo: 查询数据库获取所有启用的任务
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(SysJob sysJob) {
        // todo: 保存任务到数据库

        if (JobStatus.ENABLE.getStatus().equals(sysJob.getStatus())) {
            addScheduleJob(sysJob);
        }

        return true;
    }

    private boolean addScheduleJob(SysJob sysJob) {
        return QuartzUtil.scheduleJob(sysJob.getKey(), JobType.getJobType(sysJob.getType()), sysJob.getExpression(),
                sysJob.getInvokeTarget(), sysJob.getInvokeMethod(), JSONUtil.toBean(sysJob.getParma(), new TypeReference<Map<String, Object>>() {
                }, true));
    }
}
