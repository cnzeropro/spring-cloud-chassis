package org.zero.demo.mybatisplus;

import org.zero.demo.mybatisplus.service.BaseService;
import org.zero.iam.common.model.po.SysUserPO;

/**
 * @author zero
 * @since 2023/9/14
 */
public interface CustomService extends BaseService<SysUserPO> {
    void test();
}