package org.zero.component.spring.boot.security.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;

/**
 * @autho zero
 * @since 2021/7/20
 */
public class AnonymousConfigurerCustomizer implements Customizer<AnonymousConfigurer<HttpSecurity>> {
    @Override
    public void customize(AnonymousConfigurer<HttpSecurity> httpSecurityAnonymousConfigurer) {
        httpSecurityAnonymousConfigurer.authorities("ROLE_ANONYMOUS", "ROLE_ANON");
    }
}
