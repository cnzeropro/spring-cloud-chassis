package org.zero.component.spring.boot.security.customizer;

import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;
import org.zero.common.data.model.common.Result;
import org.zero.common.data.util.JacksonUtils;
import org.zero.common.data.util.web.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zero
 * @since 2023/7/20
 */
public class LogoutConfigurerCustomizer implements Customizer<LogoutConfigurer<HttpSecurity>> {
    @Override
    public void customize(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
        httpSecurityLogoutConfigurer.logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .permitAll();
    }

    public static class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
        /**
         * 退出处理
         */
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            // todo:
            // 删除用户缓存记录
            // 记录用户退出日志

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
                return;
            }

            // 否则默认返回json提示信息
            Result<Void> result = Result.ok("用户登出成功");
            String jsonStr = JacksonUtils.toJsonStr(result);
            ResponseUtil.writeOkJson(response, jsonStr);
        }
    }
}
