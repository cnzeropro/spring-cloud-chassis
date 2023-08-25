package org.zero.common.core.config.jasypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：
 * <a href="http://www.jasypt.org/">jasypt</a>
 * <a href="https://github.com/ulisesbocchio/jasypt-spring-boot">jasypt-spring-boot</a>
 * 自动装配：
 * {@link com.ulisesbocchio.jasyptspringbootstarter.JasyptSpringBootAutoConfiguration}
 * {@link com.ulisesbocchio.jasyptspringbootstarter.JasyptSpringCloudBootstrapConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/10/13 20:56
 */
@EnableEncryptableProperties
@Configuration(proxyBeanMethods = false)
public class JasyptConfig {
}
