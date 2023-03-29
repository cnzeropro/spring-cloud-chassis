package org.zero.component.spring.boot.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail customJobDetail() {
        // 具体任务类
        return JobBuilder.newJob(CustomJob.class)
                // 给JobDetail起一个id，不写也会自动生成唯一的TriggerKey
                .withIdentity("customJobDetail")
                // JobDetail内部的一个map，可以存储有关Job的数据，这里的数据可通过Job类中executeInternal()的参数进行获取
                .usingJobData("job_params", "Hallo JobDetail")
                // 即使没有Trigger关联时也不删除该JobDetail
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger customJobTrigger() {
        return TriggerBuilder.newTrigger()
                // 关联jobDetail
                .forJob(customJobDetail())
                .withIdentity("customJobTrigger")
                .usingJobData("trigger_params", "Hallo Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ? *"))
                .build();
    }
}
