package org.zero.demo.mybatisplus;

import org.zero.demo.mybatisplus.service.ServiceImpl;
import org.zero.iam.common.model.po.SysUserPO;

/**
 * @author zero
 * @since 2023/9/14
 */
public class CustomServiceImpl extends ServiceImpl<CustomManager, SysUserPO> implements CustomService {
    @Override
    public void test() {
        System.out.println("我是测试方法");
    }
}