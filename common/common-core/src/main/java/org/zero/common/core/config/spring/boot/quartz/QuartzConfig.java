package org.zero.common.core.config.spring.boot.quartz;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 官网：<a href="http://www.quartz-scheduler.org/">Quartz</a>
 * 自动装配：{@link QuartzAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@AutoConfigureBefore(QuartzAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class QuartzConfig {
    @Bean
    @Order(-1)
    public SchedulerFactoryBeanCustomizer contextKeyCustomizer() {
        return schedulerFactoryBean -> schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
    }
}
