package org.zero.component.spring.boot.security.customizer;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zero
 * @since 2020/5/20
 */
public class HttpBasicConfigurerCustomizer implements Customizer<HttpBasicConfigurer<HttpSecurity>> {
    @Override
    public void customize(HttpBasicConfigurer<HttpSecurity> httpSecurityHttpBasicConfigurer) {
        httpSecurityHttpBasicConfigurer.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .authenticationDetailsSource(new WebAuthenticationDetailsSource());
    }

    public static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        }
    }

    public static class CustomAuthenticationDetailsSource<T> implements AuthenticationDetailsSource<HttpServletRequest, T> {
        @Override
        public T buildDetails(HttpServletRequest context) {
            return null;
        }
    }
}
