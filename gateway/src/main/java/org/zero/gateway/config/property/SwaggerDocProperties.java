package org.zero.gateway.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zero
 * @since 2023/7/10
 */
@Data
@Component
@ConfigurationProperties("swagger")
public class SwaggerDocProperties {
    private Map<String, String> services;

    /**
     * 认证参数
     */
    private SwaggerBasic basic = new SwaggerBasic();

    @Data
    public static class SwaggerBasic {
        /**
         * 是否开启 basic 认证
         */
        private Boolean enabled;
        /**
         * 用户名
         */
        private String username;
        /**
         * 密码
         */
        private String password;
    }
}
