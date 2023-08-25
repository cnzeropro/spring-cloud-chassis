package org.zero.common.core.config.spring.data.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 官网：<a href="https://spring.io/projects/spring-data-jpa/">Spring Data JPA</a>
 * 自动装配：{@link org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration}
 *
 * @author zero
 * @since 2022/8/16
 */
@EnableJpaRepositories
@EnableJpaAuditing
@Configuration(proxyBeanMethods = false)
public class JpaConfig {
}