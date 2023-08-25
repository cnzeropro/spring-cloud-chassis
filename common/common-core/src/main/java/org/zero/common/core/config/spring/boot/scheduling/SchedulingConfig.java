package org.zero.common.core.config.spring.boot.scheduling;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@EnableScheduling
@Configuration(proxyBeanMethods = false)
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // add some scheduling tasks
    }
}
