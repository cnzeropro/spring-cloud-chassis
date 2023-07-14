package org.zero.component.mybatisplus;

import org.zero.model.UserPO;

import javax.annotation.Resource;

/**
 * @author zero
 * @since 2023/5/16
 */
public class CustomController extends BaseController<CustomService, UserPO> {

    /**
     * 当有多个类型的业务类在spring容器中，重写该set方法用于注入指定类型
     * 如果只有唯一一个，请大胆删除它
     */
    @Override
    @Resource
    public void setBaseService(CustomService baseService) {
        super.setBaseService(baseService);
    }

    public void test() {
        baseService.test();
    }
}