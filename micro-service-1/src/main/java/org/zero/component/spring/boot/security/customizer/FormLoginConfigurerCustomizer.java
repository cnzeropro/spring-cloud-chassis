package org.zero.component.spring.boot.security.customizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.util.StringUtils;
import org.zero.common.data.model.common.Result;
import org.zero.common.data.util.spring.JacksonUtils;
import org.zero.common.data.util.web.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zero
 * @since 2018/1/2
 */
@Slf4j
public class FormLoginConfigurerCustomizer implements Customizer<FormLoginConfigurer<HttpSecurity>> {
    @Override
    public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
        httpSecurityFormLoginConfigurer.loginPage("/login/form")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login/handle")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .successForwardUrl("/login/ok")
                .failureHandler(new CustomAuthenticationFailureHandler())
                .failureForwardUrl("/login/fail")
                .permitAll();
    }

    public static class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            // todo:
            // 更新用户登录记录表

            // 获取请求参数中回调地址并进行转跳
            String redirectUrl = request.getParameter("redirect_url");
            if (StringUtils.hasText(redirectUrl)) {
                response.sendRedirect(redirectUrl);
                return;
            }

            // 跳转referer地址
            String referer = request.getHeader(HttpHeaders.REFERER);
            if (StringUtils.hasText(referer)) {
                response.sendRedirect(referer);
            }
        }
    }

    public static class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            Result<Void> result = Result.fail(HttpStatus.UNAUTHORIZED, exception.getMessage());
            if (exception instanceof InsufficientAuthenticationException) {
                result.setMsg("账户权限不足");
            } else if (exception instanceof BadCredentialsException) {
                result.setMsg("账户凭证无效");
            } else if (exception instanceof UsernameNotFoundException) {
                result.setMsg("用户名或密码错误");
            } else if (exception instanceof SessionAuthenticationException) {
                result.setMsg("用户会话无效");
            } else if (exception instanceof LockedException) {
                result.setMsg("账户锁定中");
            } else if (exception instanceof DisabledException) {
                result.setMsg("账户封禁中");
            } else if (exception instanceof CredentialsExpiredException) {
                result.setMsg("账户凭证已过期");
            } else if (exception instanceof AccountExpiredException) {
                result.setMsg("账户已过期");
            }
            String jsonStr = JacksonUtils.toJsonStr(result);
            ResponseUtil.writeErrorJson(response, jsonStr);
        }
    }
}
