package org.zero.common.core.config.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://www.apolloconfig.com/">Apollo（阿波罗）</a>
 * 自动装配：{@link com.ctrip.framework.apollo.spring.boot.ApolloAutoConfiguration}
 *
 * @author cnzeropro@qq.com
 * @date 2021/6/13
 */
@EnableApolloConfig
@Configuration(proxyBeanMethods = false)
public class ApolloConfig {
}
