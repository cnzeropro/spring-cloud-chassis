package org.zero.demo.spring.boot.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(1)
@Component
public class SysInitListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String appName = event.getApplicationContext().getEnvironment().getProperty("spring.application.name", "app");
        log.info("################## System Service [{}] is Started. ##################", appName);
    }
}
