package org.zero.demo.spring.boot.security.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zero
 * @since 2023/7/20
 */
public class CorsConfigurerCustomizer implements Customizer<CorsConfigurer<HttpSecurity>> {

    @Override
    public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
        httpSecurityCorsConfigurer.configurationSource(new CustomCorsConfigurationSource());
    }

    public static class CustomCorsConfigurationSource implements CorsConfigurationSource {
        @Override
        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
            return null;
        }
    }
}
