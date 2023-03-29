package org.zero.component.spring.alibaba.seata;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.context.annotation.Configuration;

/**
 * seata使用：
 * 需要分布式事务管理的方法使用@GlobalTransactional注解即可
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/12/1 14:41
 */
@Configuration(proxyBeanMethods = false)
@GlobalTransactional
public class SeataConfig {
}
