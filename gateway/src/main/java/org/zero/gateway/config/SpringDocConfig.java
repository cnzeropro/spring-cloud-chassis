package org.zero.gateway.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.zero.gateway.config.property.SwaggerDocProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Doc 配置
 *
 * @author zero
 * @date 2022/3/26
 */
@Configuration(proxyBeanMethods = false)
public class SpringDocConfig {
    @Bean
    @Lazy
    @ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters, SwaggerDocProperties swaggerProperties) {
        List<GroupedOpenApi> groups = new ArrayList<>();
        for (String value : swaggerProperties.getServices().values()) {
            swaggerUiConfigParameters.addGroup(value);
        }
        return groups;
    }
}
