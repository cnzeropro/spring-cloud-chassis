package org.zero.component.xxl.job;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zero
 * @since 2023/2/6
 */
@Data
@ConfigurationProperties("xxl.job")
public class XxlJobProperties {
    private Admin admin;
    private String accessToken;
    private Executor executor;

    @Data
    public static class Admin {
        private String addresses;
    }

    @Data
    public static class Executor {
        private String appName;
        private String address;
        private String ip;
        private int port;
        private String logPath;
        private int logRetentionDays;
    }
}
