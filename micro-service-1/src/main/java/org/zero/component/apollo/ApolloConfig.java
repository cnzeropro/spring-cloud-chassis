package org.zero.component.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author cnzeropro@qq.com
 * @date 2021/6/13
 */
@Configuration(proxyBeanMethods = false)
@EnableApolloConfig({"app"})
public class ApolloConfig {
}
