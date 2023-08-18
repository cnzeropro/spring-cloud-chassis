package org.zero.component.spring.boot.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Setter
@Getter
@ConfigurationProperties("sys.net.http")
public class HttpProperties {
    /**
     * 连接超时时间，默认：5s
     */
    private Duration connectTimeout = Duration.ofSeconds(5);
    /**
     * 服务器返回数据（response）的超时时间，默认：1m
     */
    private Duration readTimeout = Duration.ofMinutes(1);

    /**
     * 从连接池中获取连接的超时时间，默认：1s
     */
    private Duration connectionRequestTimeout = Duration.ofSeconds(1);

    /**
     * 连接不活动的检查时间，默认：3s
     */
    private Duration validateAfterInactivity = Duration.ofSeconds(3);
    /**
     * 最大连接数，默认：150
     */
    private int maxTotal = 150;
    /**
     * 每个路由的最大默认值，默认：100
     */
    private int defaultMaxPerRoute = 100;
}
