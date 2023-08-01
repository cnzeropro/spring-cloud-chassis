package org.zero.common.data.util.web;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zero.iam.model.dto.SysUserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring Security 工具类
 */
@UtilityClass
public class SpringSecurityUtil {

    /**
     * 获取Authentication
     */
    public Authentication getAuthentication() {
        return getAuthenticationOpt().orElse(null);
    }

    /**
     * 获取Authentication Optional
     */
    public Optional<Authentication> getAuthenticationOpt() {
        return Optional.ofNullable(SecurityContextHolder.getContext()).map(SecurityContext::getAuthentication);
    }

    /**
     * 获取用户
     */
    public SysUserDetails getUser(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .filter(SysUserDetails.class::isInstance)
                .map(SysUserDetails.class::cast)
                .orElse(null);
    }

    /**
     * 获取用户名称
     */
    public String getUsername() {
        return getAuthenticationOpt().map(Authentication::getName)
                .orElse(null);
    }

    /**
     * 获取当前用户
     */
    public SysUserDetails getUser() {
        return getUser(getAuthentication());
    }

    /**
     * 获取当前用户角色信息
     */
    public Set<String> getRoles() {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> CharSequenceUtil.startWith(authority, "ROLE_"))
                .map(authority -> CharSequenceUtil.removePrefix(authority, "ROLE_"))
                .collect(Collectors.toSet());
    }

    /**
     * 获取当前用户权限信息
     */
    public Set<String> getPermissions() {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !CharSequenceUtil.startWith(authority, "ROLE_"))
                .collect(Collectors.toSet());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthenticationOpt().map(Authentication::getAuthorities)
                .orElseGet(Collections::emptyList);
    }
}
