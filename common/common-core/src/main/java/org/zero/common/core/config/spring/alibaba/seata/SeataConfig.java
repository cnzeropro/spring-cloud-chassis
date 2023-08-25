package org.zero.common.core.config.spring.alibaba.seata;

import io.seata.spring.annotation.GlobalTransactional;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.zero.common.core.factory.YamlPropertySourceFactory;

/**
 * 官网：<a href="https://seata.io/zh-cn/index.html">Seata</a>
 * 自动装配：{@link io.seata.spring.boot.autoconfigure.SeataAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/12/1 14:41
 */
@PropertySource(name = "seata-config.yml", value = "classpath:seata/seata-config.yml", encoding = "UTF-8", factory = YamlPropertySourceFactory.class)
@EnableAutoDataSourceProxy
@GlobalTransactional
@Configuration(proxyBeanMethods = false)
public class SeataConfig {
}
