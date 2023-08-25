package org.zero.demo.spring.boot.security.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

/**
 * @author zero
 * @since 2023/7/20
 */
public class HeadersConfigurerCustomizer implements Customizer<HeadersConfigurer<HttpSecurity>> {
    @Override
    public void customize(HeadersConfigurer<HttpSecurity> httpSecurityHeadersConfigurer) {
    }
}
