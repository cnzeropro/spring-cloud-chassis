package org.zero.common.core.config.spring.alibaba.nacos;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://nacos.io/zh-cn/index.html">Nacos</a>
 * 自动装配：
 * {@link com.alibaba.cloud.nacos.NacosServiceAutoConfiguration}
 * {@link com.alibaba.cloud.nacos.NacosConfigAutoConfiguration}
 *
 * @author Zero
 * @since 2022/7/16
 */
@Configuration(proxyBeanMethods = false)
// 从 Spring Cloud Edgware 版本开始，实际上已经不需要添加 @EnableDiscoveryClient 注解，只需引入 Spring Cloud 注册发现相关组件，就会自动开启注册发现的功能
@EnableDiscoveryClient
public class NacosConfig {
}
