package org.zero.common.core.config.spring.boot.retry;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/10/11
 */
@EnableRetry
@Configuration(proxyBeanMethods = false)
public class RetryConfig {
}
