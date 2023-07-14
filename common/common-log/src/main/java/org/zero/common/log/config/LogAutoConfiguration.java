package org.zero.common.log.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.zero.common.core.feign.RemoteLogService;
import org.zero.common.log.aspect.SysLogAspect;
import org.zero.common.log.event.SysLogListener;

/**
 * 日志自动装配
 *
 * @author zero
 * @date 2022/10/1
 */
@EnableAsync
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class LogAutoConfiguration {

    /**
     * 注入日志监听器
     */
    @Bean
    SysLogListener sysLogListener(RemoteLogService remoteLogService) {
        return new SysLogListener(remoteLogService);
    }

    /**
     * 注入日志切面
     */
    @Bean
    SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }
}
