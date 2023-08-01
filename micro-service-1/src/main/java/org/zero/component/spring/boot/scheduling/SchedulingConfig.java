package org.zero.component.spring.boot.scheduling;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 自定义或者实现{@link org.springframework.scheduling.annotation.SchedulingConfigurer}
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Configuration(proxyBeanMethods = false)
@EnableScheduling
public class SchedulingConfig {
    // add some scheduling tasks
}
