package org.zero.component.spring.boot.retry;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/10/11
 */
@Configuration(proxyBeanMethods = false)
@EnableRetry
public class RetryConfig {
}
