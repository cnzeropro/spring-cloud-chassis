package org.zero.component.spring.boot.security;

import cn.hutool.core.map.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Zero
 * @since 2020/7/7
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "sys.web.security")
public class SpringSecurityProperties {
    /**
     * 忽略的URL
     */
    private String[] ignoredUrl = new String[0];

    /**
     * 自定义header
     */
    private Map<String, String> header = MapUtil.of("access-token-name", "X-Access-Token");

    /**
     * 同一账号同时登录最大用户数
     */
    private Integer maxSession = 1;
}
