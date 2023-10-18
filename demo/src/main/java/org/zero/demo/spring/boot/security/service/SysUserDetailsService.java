package org.zero.demo.spring.boot.security.service;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.zero.iam.common.model.dto.security.SecurityLoginUser;
import org.zero.iam.common.model.dto.SysUserInfoDTO;
import org.zero.iam.common.model.po.SysPermissionPO;
import org.zero.iam.common.model.po.SysRolePO;
import org.zero.iam.common.model.po.SysUserPO;

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
        SysRolePO[] sysRoles = sysUserInfo.getSysRoles();
        if (ArrayUtil.isNotEmpty(sysRoles)) {
            auths.addAll(Arrays.stream(sysRoles).map(SysRolePO::getName).map(n -> "ROLE_" + n).collect(Collectors.toList()));
        }
        // 权限
        SysPermissionPO[] sysPermissions = sysUserInfo.getSysPermissions();
        if (ArrayUtil.isNotEmpty(sysPermissions)) {
            auths.addAll(Arrays.stream(sysPermissions).map(SysPermissionPO::getName).collect(Collectors.toList()));
        }
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(auths.toArray(new String[0]));

        SysUserPO user = sysUserInfo.getSysUser();
        // 构造security用户
        return new SecurityLoginUser(user.getId(), user.getName(),
                "{bcrypt}" + user.getPassword(), true, true, true,
                !user.getLocked(), authorities);
    }

    default boolean support(String clientId, String grantType) {
        return true;
    }

    default UserDetails loadUserByUser(SysUserPO sysUser) {
        return loadUserByUsername(sysUser.getName());
    }

    @Override
    default int getOrder() {
        return 0;
    }
}
