package org.zero.common.core.config.spring.boot.web.embedded.tomcat;

import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
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
     * 解决两个问题：
     * <p>
     * 1、内嵌Tomcat默认不注册JspServet，所以一般需要引入tomcat-embed-jasper来支持JSP，但是引入这个jar包后，会报部分jar包找不到的问题。因此关闭Tomcat的jar包扫描器扫描Manifest。
     * <p>
     * 2、tomcat8.5以上版本严格按照RFC3986规范进行访问解析（RFC3986中指定这些字符为保留字符：!*’();:@&=+$,/?#[]），所以需要进行特殊字符处理。
     * <p>
     * 方案一：配置类配置（本例）
     * 方案二：配置文件配置（推荐），参考：server.tomcat.relaxed-path-chars、server.tomcat.relaxed-query-chars
     * 方案三：在 conf/catalina.properties 中添加一行：org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true 或者 tomcat.util.http.parser.HttpParser.requestTargetAllow=|{}[]
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                // 解决：tomcat-embed-jasper（对jsp的支持依赖）引用后，提示xxx.jar找不到的问题
                ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
            }
        };
        factory.addConnectorCustomizers(connector -> {
            // 允许的特殊字符
            // 对与Spring MVC而言，以下字符便够用了
            connector.setProperty("relaxedPathChars", "[]{}");
            connector.setProperty("relaxedQueryChars", "[]{}");
            // 对与其他特殊URL，可能需要更多字符
            // connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
            // connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
        });
        return factory;
    }
}
