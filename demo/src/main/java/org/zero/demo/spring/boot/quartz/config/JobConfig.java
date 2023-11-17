package org.zero.demo.spring.boot.quartz.config;

import org.springframework.context.annotation.Configuration;
import org.zero.demo.spring.boot.quartz.model.SysJob;
import org.zero.demo.spring.boot.quartz.service.SysJobService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author zero
 * @since 2022/10/26
 */
@Configuration(proxyBeanMethods = false)
public class JobConfig {
    @Resource
    private SysJobService sysJobService;

    /**
     * todo: 应用启动时主动添加任务到Quartz
     */
    @PostConstruct
    public void initJob() {
        List<SysJob> sysJobs = sysJobService.listEnabled();
        for (SysJob sysJob : sysJobs) {

        }
    }
}
