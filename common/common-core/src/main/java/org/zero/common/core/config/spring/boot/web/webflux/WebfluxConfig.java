package org.zero.common.core.config.spring.boot.web.webflux;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * 自动装配：{@link org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration}
 *
 * @author Zero
 * @since 2021/2/23
 */
@EnableWebFlux
@Configuration(proxyBeanMethods = false)
public class WebfluxConfig {
}
