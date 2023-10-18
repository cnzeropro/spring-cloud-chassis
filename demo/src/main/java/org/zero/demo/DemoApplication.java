package org.zero.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        String appName = environment.getProperty("spring.application.name", "app");
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "/");
        log.info("\n----------------------------------------------------------\n"
                        + "\tApplication [{}] is running! Access urls:\n"
                        + "\tLocal: \t\thttp://127.0.0.1:{}{}\n"
                        + "\tExternal: \thttp://{}:{}{}\n"
                        + "----------------------------------------------------------"
                , appName, port, contextPath, ip, port, contextPath);

    }

}