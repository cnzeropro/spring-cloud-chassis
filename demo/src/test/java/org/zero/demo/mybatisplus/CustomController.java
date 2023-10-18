package org.zero.demo.mybatisplus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zero.iam.common.model.po.SysUserPO;

import javax.annotation.Resource;

/**
 * @author zero
 * @since 2023/5/16
 */
@RestController
@RequestMapping("/custom")
public class CustomController extends BaseController<CustomService, SysUserPO> {

    /**
     * 当有多个类型的业务类在spring容器中，重写该set方法用于注入指定类型
     * 如果只有唯一一个，请大胆删除它
     */
    @Override
    @Resource
    public void setBaseService(CustomService customService) {
        super.setBaseService(customService);
    }

    @GetMapping("/test")
    public void test() {
        baseService.test();
    }
}