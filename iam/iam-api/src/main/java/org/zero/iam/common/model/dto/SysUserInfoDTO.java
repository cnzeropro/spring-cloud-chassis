package org.zero.iam.common.model.dto;

import lombok.Data;
import org.zero.iam.common.model.po.SysPermissionPO;
import org.zero.iam.common.model.po.SysRolePO;
import org.zero.iam.common.model.po.SysUserPO;

import java.io.Serializable;

/**
 * @author zero
 * @date 2019/2/10
 */
@Data
public class SysUserInfoDTO implements Serializable {
    /**
     * 用户基本信息
     */
    private SysUserPO sysUser;
    /**
     * 用户权限
     */
    private SysPermissionPO[] sysPermissions;
    /**
     * 用户角色
     */
    private SysRolePO[] sysRoles;
}
