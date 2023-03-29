package org.zero.component.spring.boot.web.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Zero
 */
@Configuration
public class TomcatWebSocketConfig {
    /**
     * 注入ServerEndpointExporter
     * 该bean会自动注册使用了@ServerEndpoint注解声明的Websocket Endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
