package org.zero.component.mybatisplus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zero.model.StudentPO;

/**
 * @author zero
 * @since 2023-03-07 09:07:48
 */
@Slf4j
@Service
public class CustomServiceImpl extends ServiceImpl<BaseMapper<StudentPO>, StudentPO> implements CustomService {
    @Override
    public void test() {
        System.out.println("我是测试方法");
    }
}