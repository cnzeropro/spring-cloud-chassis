/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zero.iam.model.dto;

import lombok.Data;
import org.zero.iam.model.po.SysPermission;
import org.zero.iam.model.po.SysRole;
import org.zero.iam.model.po.SysUser;

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
    private SysUser sysUser;
    /**
     * 用户权限
     */
    private SysPermission[] sysPermissions;
    /**
     * 用户角色
     */
    private SysRole[] sysRoles;
}
