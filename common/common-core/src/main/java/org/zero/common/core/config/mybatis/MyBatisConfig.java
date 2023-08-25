package org.zero.common.core.config.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://mybatis.org/mybatis-3/zh/index.html">MyBatis</a>
 * 自动装配：{@link org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration}
 *
 * @author cnzeropro@qq.com
 * @since 2019/4/17
 */
@MapperScan({"org.zero.**.mapper"})
@Configuration(proxyBeanMethods = false)
public class MyBatisConfig {
}