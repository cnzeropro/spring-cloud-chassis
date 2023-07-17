package org.zero.component.spring.alibaba.seata;

import io.seata.spring.annotation.GlobalTransactional;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.zero.common.core.factory.YamlPropertySourceFactory;

/**
 * seata使用：
 * 需要分布式事务管理的方法使用@GlobalTransactional注解即可
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/12/1 14:41
 */
@PropertySource(name = "seata-config.yml", value = "classpath:seata/seata-config.yml", encoding = "UTF-8", factory = YamlPropertySourceFactory.class)
@Configuration(proxyBeanMethods = false)
@EnableAutoDataSourceProxy
@GlobalTransactional
public class SeataConfig {
}
