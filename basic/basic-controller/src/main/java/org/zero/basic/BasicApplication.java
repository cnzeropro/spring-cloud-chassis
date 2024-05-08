package org.zero.basic;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;

/**
 * @author zero
 * @since 2022/1/23
 */
@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class BasicApplication {
    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BasicApplication.class, args);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        String appName = environment.getProperty("spring.application.name", "app");
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "/");
        log.info("\n----------------------------------------------------------\n"
                        + "\tApplication [{}] is running! Access urls:\n"
                        + "\tLocal: \t\thttp://127.0.0.1:{}{}\n"
                        + "\tExternal: \thttp://{}:{}{}\n"
                        + "----------------------------------------------------------\n"
                , appName, port, contextPath, ip, port, contextPath);
    }
}
