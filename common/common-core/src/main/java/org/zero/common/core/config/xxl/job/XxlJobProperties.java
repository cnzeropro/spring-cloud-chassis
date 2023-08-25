package org.zero.common.core.config.xxl.job;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zero
 * @since 2021/2/6
 */
@Setter
@Getter
@ConfigurationProperties("sys.xxl.job")
public class XxlJobProperties {
    private Admin admin = new Admin();
    private String accessToken;
    private Executor executor = new Executor();

    @Setter
    @Getter
    public static class Admin {
        private String addresses;
    }

    @Setter
    @Getter
    public static class Executor {
        private String appName;
        private String address;
        private String ip;
        private Integer port = 9999;
        private String logPath;
        private Integer logRetentionDays = 30;
    }
}
