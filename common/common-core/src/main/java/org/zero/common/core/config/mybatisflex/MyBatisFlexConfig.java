package org.zero.common.core.config.mybatisflex;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://mybatis-flex.com/">MyBatis-Flex</a>
 * 自动装配：{@link com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration}
 *
 * @author cnzeropro@qq.com
 * @since 2022/5/16
 */
@MapperScan({"org.zero.**.mapper"})
@Configuration(proxyBeanMethods = false)
public class MyBatisFlexConfig {
}
