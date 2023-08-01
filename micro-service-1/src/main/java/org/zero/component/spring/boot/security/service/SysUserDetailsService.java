package org.zero.component.spring.boot.security.service;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.zero.iam.model.dto.SysUserDetails;
import org.zero.iam.model.dto.SysUserInfoDTO;
import org.zero.iam.model.po.SysPermission;
import org.zero.iam.model.po.SysRole;
import org.zero.iam.model.po.SysUser;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zero
 * @since 2021/7/10
 */
public interface SysUserDetailsService extends UserDetailsService, Ordered {
    default UserDetails getUserDetails(SysUserInfoDTO sysUserInfo) {
        Set<String> auths = new HashSet<>();
        // 角色
        SysRole[] sysRoles = sysUserInfo.getSysRoles();
        if (ArrayUtil.isNotEmpty(sysRoles)) {
            auths.addAll(Arrays.stream(sysRoles).map(SysRole::getName).map(n -> "ROLE_" + n).collect(Collectors.toList()));
        }
        // 权限
        SysPermission[] sysPermissions = sysUserInfo.getSysPermissions();
        if (ArrayUtil.isNotEmpty(sysPermissions)) {
            auths.addAll(Arrays.stream(sysPermissions).map(SysPermission::getName).collect(Collectors.toList()));
        }
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(auths.toArray(new String[0]));

        SysUser user = sysUserInfo.getSysUser();
        // 构造security用户
        return new SysUserDetails(user.getId(), user.getName(),
                "{bcrypt}" + user.getPassword(), true, true, true,
                !user.getLocked(), authorities);
    }

    default boolean support(String clientId, String grantType) {
        return true;
    }

    default UserDetails loadUserByUser(SysUser sysUser) {
        return loadUserByUsername(sysUser.getName());
    }

    @Override
    default int getOrder() {
        return 0;
    }
}
