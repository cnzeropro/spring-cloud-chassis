package org.zero.component.spring.boot.security;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.zero.component.spring.boot.security.model.SysPermission;
import org.zero.component.spring.boot.security.model.SysRole;
import org.zero.component.spring.boot.security.model.SysUser;
import org.zero.component.spring.boot.security.model.SysUserDetails;
import org.zero.component.spring.boot.security.model.UserInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yufa.wang (yufa.wang@ronganchina.com)
 * @since 2023/7/10
 */
public interface SysUserDetailsService extends UserDetailsService, Ordered {

    default boolean support(String clientId, String grantType) {
        return true;
    }

    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    default UserDetails getUserDetails(UserInfo userInfo) {
        Set<String> auths = new HashSet<>();
        // 角色
        SysRole[] sysRoles = userInfo.getSysRoles();
        if (ArrayUtil.isNotEmpty(sysRoles)) {
            auths.addAll(Arrays.stream(sysRoles).map(SysRole::getName).map(n -> "ROLE_" + n).collect(Collectors.toList()));
        }
        // 权限
        SysPermission[] sysPermissions = userInfo.getSysPermissions();
        if (ArrayUtil.isNotEmpty(sysPermissions)) {
            auths.addAll(Arrays.stream(sysPermissions).map(SysPermission::getName).collect(Collectors.toList()));
        }
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(auths.toArray(new String[0]));

        SysUser user = userInfo.getSysUser();
        // 构造security用户
        return new SysUserDetails(user.getId(), user.getName(),
                "{bcrypt}" + user.getPassword(), true, true, true,
                !user.getLocked(), authorities);
    }

    default UserDetails loadUserByUser(SysUser sysUser) {
        return this.loadUserByUsername(sysUser.getName());
    }
}
