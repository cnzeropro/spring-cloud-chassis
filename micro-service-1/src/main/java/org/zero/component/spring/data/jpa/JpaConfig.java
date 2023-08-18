package org.zero.component.spring.data.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author zero
 * @since 2023/8/16
 */
@EnableJpaRepositories
@EnableJpaAuditing
@Configuration(proxyBeanMethods = false)
public class JpaConfig {
}