package org.zero.demo.spring.boot.security.filter;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zero.demo.spring.boot.security.service.SysUserDetailsService;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器
 *
 * @author zero
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${sys.web.security.header.access-token-name:X-Access-Token}")
    private String accessTokenName;

    @Resource
    private SysUserDetailsService sysUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从header中获取token
        String accessToken = request.getHeader(accessTokenName);
        if (!StringUtils.hasText(accessToken)) {
            accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.hasText(accessToken)) {
                // 没有携带token，传递给Security验证
                filterChain.doFilter(request, response);
                return;
            }
            accessToken = StrUtil.removePrefix(accessToken, "Basic ");
        }


        // todo:
        // 从token中获取用户信息
        // 通过用户信息查询用户
        UserDetails userDetails = sysUserDetailsService.loadUserByUsername("username");
        // set到SecurityContext中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
