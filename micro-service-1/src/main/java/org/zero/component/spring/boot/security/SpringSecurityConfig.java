package org.zero.component.spring.boot.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;
import org.zero.component.spring.boot.security.customizer.ExceptionHandlingConfigurerCustomizer;
import org.zero.component.spring.boot.security.customizer.LogoutConfigurerCustomizer;
import org.zero.component.spring.boot.security.filter.JwtTokenFilter;

import javax.annotation.Resource;

/**
 * spring security配置
 *
 * @author zero
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties({SpringSecurityProperties.class})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private CorsFilter corsFilter;
    @Resource
    private JwtTokenFilter jwtTokenFilter;
    @Resource
    private SpringSecurityProperties springSecurityProperties;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CORS（跨域资源共享）
                .cors()
                .disable()
                // CSRF
                .csrf()
                .disable()

                // HTTP响应标头
                .headers()
                // 避免iframe同源无法登录
                .frameOptions().sameOrigin()
                // 缓存禁用
                .cacheControl().disable()

                // session管理
                .and().sessionManagement()
                // 基于token，所以不通过Session获取SecurityContext
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                /* 授权请求
                 * access：指定SpEl表达式结果为true时可以访问
                 * fullyAuthenticated：用户完全认证时可以访问（非remember-me下自动登录）
                 * hasAnyAuthority：如果用户权限与指定权限中的任意一个匹配，则可以访问
                 * hasAnyRole：如果用户角色与指定角色中的任意一个匹配，则可以访问
                 * hasAuthority：如果用户权限和指定权限匹配，则可以访问
                 * hasRole：如果用户角色和指定角色匹配，则可以访问
                 * hasIpAddress：如果用户IP和指定IP匹配，则可以访问
                 * rememberMe：允许通过remember-me登录的用户访问
                 * anonymous：允许匿名用户访问，不允许已登入用户访问
                 * authenticated：用户登录后可访问
                 * permitAll：用户可以任意访问
                 * denyAll：所有用户不能访问
                 */
                .and().authorizeRequests()
                // 配置
                .antMatchers(springSecurityProperties.getIgnoredUrl()).permitAll()
                // 登录注册相关
                .antMatchers("/login", "/register", "/captcha/**", "/token/**").permitAll()
                // 静态资源相关
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                // 三方jar相关
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()

                // 越权处理
                .and().exceptionHandling()
                .authenticationEntryPoint(new ExceptionHandlingConfigurerCustomizer.CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new ExceptionHandlingConfigurerCustomizer.CustomAccessDeniedHandler())
                // 登出处理
                .and().logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new LogoutConfigurerCustomizer.CustomLogoutSuccessHandler())
                .permitAll()
                // 添加 filter
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsFilter, JwtTokenFilter.class)
                .addFilterBefore(corsFilter, LogoutFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        // auth.inMemoryAuthentication()
        //         .withUser("zero")
        //         .password("abc123")
        //         .roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
