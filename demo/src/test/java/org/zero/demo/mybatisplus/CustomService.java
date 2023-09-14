package org.zero.demo.mybatisplus;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zero.iam.model.po.SysUserPO;

/**
 * @author zero
 * @since 2023-03-07 09:07:48
 */
public interface CustomService extends IService<SysUserPO> {
    void test();
}