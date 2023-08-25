package org.zero.common.core.config.spring.boot.web.socket.tomcat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 自动装配：{@link org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration}
 *
 * @author Zero
 */
@Configuration(proxyBeanMethods = false)
public class WebSocketConfig {
    /**
     * 注入ServerEndpointExporter
     * 该bean会自动注册使用了@ServerEndpoint注解声明的Websocket Endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
