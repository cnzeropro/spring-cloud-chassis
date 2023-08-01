/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package org.zero.common.swagger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.List;

/**
 * swagger配置
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@ConditionalOnMissingClass("org.springframework.cloud.gateway.config.GatewayAutoConfiguration")
public class SwaggerConfiguration {
    private final SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI openAPI(ServiceInstance serviceInstance) {
        OpenAPI openAPI = new OpenAPI().info(new Info().title(swaggerProperties.getTitle()));
        // oauth2.0 password
        openAPI.schemaRequirement(HttpHeaders.AUTHORIZATION, securityScheme());
        // servers
        // List<Server> servers = swaggerProperties.getServices()
        //         .entrySet()
        //         .stream()
        //         .filter(entry -> Objects.equals(serviceInstance.getServiceId(), entry.getKey()))
        //         .map(Map.Entry::getValue)
        //         .map(path -> (new Server().url(swaggerProperties.getGateway() + "/" + path)))
        //         .collect(Collectors.toList());
        String path = swaggerProperties.getServices().get(serviceInstance.getServiceId());
        List<Server> servers = Collections.singletonList(new Server().url(swaggerProperties.getGateway() + "/" + path));
        openAPI.servers(servers);
        return openAPI;
    }

    private SecurityScheme securityScheme() {
        OAuthFlow oAuthFlow = new OAuthFlow();
        oAuthFlow.setTokenUrl(swaggerProperties.getTokenUrl());
        oAuthFlow.setScopes(new Scopes().addString(swaggerProperties.getScope(), swaggerProperties.getScope()));
        OAuthFlows oauthFlows = new OAuthFlows();
        oauthFlows.password(oAuthFlow);
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.OAUTH2);
        securityScheme.setFlows(oauthFlows);
        return securityScheme;
    }
}
