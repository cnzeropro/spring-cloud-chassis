package org.zero.common.core.config.spring.boot.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 自动装配：{@link org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration}
 *
 * @author Zero
 * @since 2022/7/7
 */
@Configuration(proxyBeanMethods = false)
public class MessageSourceConfig {

    /**
     * 建议使用配置文件配置，而不是使用Java Configuration注入
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        return messageSource;
    }
}
