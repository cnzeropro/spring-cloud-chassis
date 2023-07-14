package org.zero.component.mybatisplus;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zero.model.UserPO;

/**
 * @author zero
 * @since 2023-03-07 09:07:48
 */
public interface CustomService extends IService<UserPO> {
    void test();
}