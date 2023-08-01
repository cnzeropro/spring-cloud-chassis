package org.zero.component.spring.boot.security.customizer;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.zero.common.data.model.common.Result;
import org.zero.common.data.util.JacksonUtils;
import org.zero.common.data.util.web.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zero
 * @since 2021/7/20
 */
public class ExceptionHandlingConfigurerCustomizer implements Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {
    @Override
    public void customize(ExceptionHandlingConfigurer<HttpSecurity> httpSecurityExceptionHandlingConfigurer) {
        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    /**
     * 处理匿名用户访问无权限资源时的异常
     */
    public static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            Result<Void> result = Result.fail(HttpStatus.UNAUTHORIZED, authException.getMessage());
            String jsonStr = JacksonUtils.toJsonStr(result);
            ResponseUtil.writeErrorJson(response, jsonStr);
        }
    }

    /**
     * 处理认证过的用户访问无权限资源时的异常
     */
    public static class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            Result<Void> result = Result.fail(HttpStatus.FORBIDDEN, accessDeniedException.getMessage());
            String jsonStr = JacksonUtils.toJsonStr(result);
            ResponseUtil.writeErrorJson(response, jsonStr);
        }
    }
}
