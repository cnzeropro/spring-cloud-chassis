package org.zero.component.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author cnzeropro@qq.com
 * @since 2023/4/17
 */
@Configuration(proxyBeanMethods = false)
@MapperScan({"org.zero.**.mapper"})
public class MyBatisConfig {
}