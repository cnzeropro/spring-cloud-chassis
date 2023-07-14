package org.zero.gateway.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * 网关配置文件
 *
 * @author zero
 * @date 2020/10/4
 */
@Data
@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayProperties {

    /**
     * 网关解密登录前端密码 秘钥 {@link org.zero.gateway.filter.PasswordDecoderFilter}
     */
    private String encodeKey;

    /**
     * 网关不需要校验验证码的客户端 {@link org.zero.gateway.filter.ValidateCaptchaGatewayFilter}
     */
    private List<String> ignoreClients;

}
