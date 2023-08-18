package org.zero.component.spring.boot.security.customizer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.zero.common.data.model.common.Result;
import org.zero.common.data.util.spring.JacksonUtils;
import org.zero.common.data.util.web.ResponseUtil;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author zero
 * @since 2022/6/12
 */
public class SessionManagementConfigurerCustomizer implements Customizer<SessionManagementConfigurer<HttpSecurity>> {
    /**
     * 同一账号同时登录最大用户数
     */
    public static final int DEFAULT_MAX_SESSION = 1;

    @Override
    public void customize(SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer) {
        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .maximumSessions(DEFAULT_MAX_SESSION)
                .expiredSessionStrategy(new CustomSessionInformationExpiredStrategy());
    }

    public static class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
        @Override
        public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
            Result<Void> result = Result.fail("账户已在别处登录");
            String jsonStr = JacksonUtils.toJsonStr(result);
            ResponseUtil.writeErrorJson(event.getResponse(), jsonStr);
        }
    }
}
