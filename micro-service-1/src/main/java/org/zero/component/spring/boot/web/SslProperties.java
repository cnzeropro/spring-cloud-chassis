package org.zero.component.spring.boot.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Setter
@Getter
@ConfigurationProperties("sys.net.ssl")
public class SslProperties {
    /**
     * 传输协议，默认：TLSv1.2
     */
    private String protocol = "TLSv1.2";
    /**
     * 证书类型，PKCS12、JKS等等，默认JKS
     * <p>
     * PKCS12：.p12、.pfx
     * JKS：.jks
     */
    private String type = "JKS";
    /**
     * 证书文件
     */
    private String keyFile;
    /**
     * 密码
     */
    private String password;

    /**
     * 证书文件
     */
    private String trustFile;
    /**
     * 密码
     */
    private String trustPassword;
}
