package org.zero.component.spring.boot.security;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.StaticLog;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.zero.common.data.util.web.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 服务安全相关配置
 *
 * @author zero
 * @date 2022/11/11
 */
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * spring security 默认的安全策略
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests -> authorizeRequests.antMatchers("/token/*")
                        // 开放自定义的部分端点
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .headers()
                .frameOptions()
                // 避免iframe同源无法登录
                .sameOrigin()
                .and()
                // 表单登录个性化
                .apply(new FormIdentityLoginConfigurer());
        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new CustomDaoAuthenticationProvider());
        return http.build();
    }

    /**
     * 暴露静态资源
     */
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        http.requestMatchers(matchers -> matchers.antMatchers("/actuator/**", "/css/**", "/error"))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .requestCache()
                .disable()
                .securityContext()
                .disable()
                .sessionManagement()
                .disable();
        return http.build();
    }

    static final class FormIdentityLoginConfigurer
            extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {
        @Override
        public void init(HttpSecurity http) throws Exception {
            http.formLogin(formLogin -> {
                        formLogin.loginPage("/token/login");
                        formLogin.loginProcessingUrl("/token/form");
                        formLogin.failureHandler((request, response, exception) -> {
                            StaticLog.debug("Form login failed: {}", exception.getLocalizedMessage());
                            String url = HttpUtil.encodeParams(String.format("/token/login?error=%s", exception.getMessage()), CharsetUtil.CHARSET_UTF_8);
                            response.sendRedirect(url);
                        });

                    })
                    .logout()
                    // SSO登出成功处理
                    .logoutSuccessHandler((request, response, authentication) -> {
                        if (response == null) {
                            return;
                        }
                        // 获取请求参数中是否包含 回调地址
                        String redirectUrl = request.getParameter("redirect_url");
                        if (StringUtils.hasText(redirectUrl)) {
                            response.sendRedirect(redirectUrl);
                        } else if (StringUtils.hasText(request.getHeader(HttpHeaders.REFERER))) {
                            // 默认跳转referer 地址
                            String referer = request.getHeader(HttpHeaders.REFERER);
                            response.sendRedirect(referer);
                        }
                    })
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .and()
                    .csrf()
                    .disable();
        }
    }

    static class CustomDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
        private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
        private static final BasicAuthenticationConverter basicConvert = new BasicAuthenticationConverter();
        private PasswordEncoder passwordEncoder;
        private volatile String userNotFoundEncodedPassword;

        private UserDetailsService userDetailsService;

        private UserDetailsPasswordService userDetailsPasswordService;

        public CustomDaoAuthenticationProvider() {
            setMessageSource(SpringUtil.getBean("securityMessageSource"));
            setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        }

        @Override
        protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
            // app 模式不用校验密码
            String grantType = Optional.ofNullable(RequestUtil.getHttpServletRequest())
                    .map(request -> request.getParameter(OAuth2ParameterNames.GRANT_TYPE))
                    .orElse("");
            if (Objects.equals("app", grantType)) {
                return;
            }

            Object credentials = authentication.getCredentials();
            if (Objects.isNull(credentials)) {
                this.logger.debug("Failed to authenticate since no credentials provided");
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
            String presentedPassword = credentials.toString();
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                this.logger.debug("Failed to authenticate since password does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }

        @Override
        protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
            if (Objects.isNull(this.userNotFoundEncodedPassword)) {
                this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
            }

            HttpServletRequest request = Optional.ofNullable(RequestUtil.getHttpServletRequest())
                    .orElseThrow(() -> new InternalAuthenticationServiceException("web request is empty"));

            Map<String, String> paramMap = ServletUtil.getParamMap(request);
            String grantType = paramMap.get(OAuth2ParameterNames.GRANT_TYPE);
            String clientId = paramMap.get(OAuth2ParameterNames.CLIENT_ID);
            if (!StringUtils.hasText(clientId)) {
                clientId = basicConvert.convert(request).getName();
            }

            String finalClientId = clientId;
            try {
                return SpringUtil.getBeansOfType(SysUserDetailsService.class)
                        .values()
                        .stream()
                        .filter(service -> service.support(finalClientId, grantType))
                        .max(Comparator.comparingInt(Ordered::getOrder))
                        .map(sysUserDetailsService -> sysUserDetailsService.loadUserByUsername(username))
                        .orElseThrow(() -> new InternalAuthenticationServiceException("UserDetailsService error, maybe not register"));
            } catch (UsernameNotFoundException e) {
                Object credentials = authentication.getCredentials();
                if (Objects.nonNull(credentials)) {
                    String presentedPassword = credentials.toString();
                    this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
                }
                throw e;
            } catch (InternalAuthenticationServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new InternalAuthenticationServiceException(e.getMessage(), e);
            }
        }

        @Override
        protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
            boolean upgradeEncoding = Objects.nonNull(this.userDetailsPasswordService)
                    && this.passwordEncoder.upgradeEncoding(user.getPassword());
            if (upgradeEncoding) {
                String presentedPassword = authentication.getCredentials().toString();
                String newPassword = this.passwordEncoder.encode(presentedPassword);
                user = this.userDetailsPasswordService.updatePassword(user, newPassword);
            }
            return super.createSuccessAuthentication(principal, authentication, user);
        }

        public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
            Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
            this.passwordEncoder = passwordEncoder;
            this.userNotFoundEncodedPassword = null;
        }

        protected PasswordEncoder getPasswordEncoder() {
            return this.passwordEncoder;
        }

        public void setUserDetailsService(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        protected UserDetailsService getUserDetailsService() {
            return this.userDetailsService;
        }

        public void setUserDetailsPasswordService(UserDetailsPasswordService userDetailsPasswordService) {
            this.userDetailsPasswordService = userDetailsPasswordService;
        }
    }
}
