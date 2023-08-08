package org.zero.component.spring.boot.scheduling;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 自定义或者实现{@link org.springframework.scheduling.annotation.SchedulingConfigurer}
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@EnableScheduling
@Configuration(proxyBeanMethods = false)
public class SchedulingConfig implements SchedulingConfigurer {
    // add some scheduling tasks
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    }
}
