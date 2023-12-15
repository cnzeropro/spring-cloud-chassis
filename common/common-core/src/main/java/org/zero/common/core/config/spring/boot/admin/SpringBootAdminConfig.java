package org.zero.common.core.config.spring.boot.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.context.annotation.Configuration;

/**
 * @author zero
 * @since 2022/12/14
 */
@EnableAdminServer
@Configuration(proxyBeanMethods = false)
public class SpringBootAdminConfig {
}
