package org.zero.demo.mybatisplus;

import org.zero.demo.mybatisplus.manager.ManagerImpl;
import org.zero.iam.model.po.SysUserPO;

/**
 * @author zero
 * @since 2023/9/14
 */
public class CustomManagerImpl extends ManagerImpl<CustomMapper, SysUserPO> implements CustomManager {
}
