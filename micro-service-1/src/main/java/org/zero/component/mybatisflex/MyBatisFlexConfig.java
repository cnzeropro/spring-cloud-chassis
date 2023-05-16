package org.zero.component.mybatisflex;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author cnzeropro@qq.com
 * @since 2023/5/16
 */
@Configuration(proxyBeanMethods = false)
@MapperScan({"org.zero.**.mapper"})
public class MyBatisFlexConfig {
}
