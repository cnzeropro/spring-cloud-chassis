package org.zero.demo.mybatisplus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zero.iam.model.po.SysUser;

/**
 * @author zero
 * @since 2023-03-07 09:07:48
 */
@Slf4j
@Service
public class CustomServiceImpl extends ServiceImpl<BaseMapper<SysUser>, SysUser> implements CustomService {
    @Override
    public void test() {
        System.out.println("我是测试方法");
    }
}