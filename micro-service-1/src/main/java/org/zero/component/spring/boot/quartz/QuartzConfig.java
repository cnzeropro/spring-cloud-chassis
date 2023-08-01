package org.zero.component.spring.boot.quartz;

import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 自动装配，参见：{@link org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Configuration(proxyBeanMethods = false)
public class QuartzConfig {
    @Bean
    @Order(-1)
    public SchedulerFactoryBeanCustomizer contextKeyCustomizer() {
        return schedulerFactoryBean -> schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
    }
}
