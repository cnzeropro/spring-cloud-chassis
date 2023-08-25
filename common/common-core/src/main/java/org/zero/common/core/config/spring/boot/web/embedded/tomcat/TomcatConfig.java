package org.zero.common.core.config.spring.boot.web.embedded.tomcat;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zero
 * @since 2022/1/20
 */
@Configuration(proxyBeanMethods = false)
public class TomcatConfig {
    /**
     * tomcat8.5以上版本严格按照RFC3986规范进行访问解析（RFC3986中指定这些字符为保留字符：!*’();:@&=+$,/?#[]），所以需要进行特殊字符处理
     * <p>
     * 方案一：配置类配置（本例）
     * 方案二：配置文件配置（推荐），参考：server.tomcat.relaxed-path-chars、server.tomcat.relaxed-query-chars
     * 方案三：在 conf/catalina.properties 中添加一行：org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true 或者 tomcat.util.http.parser.HttpParser.requestTargetAllow=|{}[]
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            // 允许的特殊字符
            connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
            connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
        });
        return factory;
    }
}
