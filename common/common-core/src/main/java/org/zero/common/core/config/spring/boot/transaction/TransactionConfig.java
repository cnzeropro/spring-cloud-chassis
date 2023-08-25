package org.zero.common.core.config.spring.boot.transaction;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 自动装配：{@link org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration}
 *
 * @author zero
 * @since 2023/2/27
 */
@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement(proxyTargetClass = true)
public class TransactionConfig {
}
