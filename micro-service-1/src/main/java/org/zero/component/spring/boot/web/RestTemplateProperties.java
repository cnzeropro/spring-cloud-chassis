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
@ConfigurationProperties("rest")
public class RestTemplateProperties {
    /**
     * 传输协议，默认：TLSv1.2
     */
    private String protocol = "TLSv1.2";
    /**
     * 连接超时时间，单位：ms，默认：5s
     */
    private int connectTimeout = 5000;
    /**
     * 服务器返回数据（response）的超时时间，单位：ms，默认：1m5s
     */
    private int readTimeout = 65000;

    /**
     * 从连接池中获取连接的超时时间，单位：ms，默认：1s
     */
    private int connectionRequestTimeout = 1000;

    /**
     * 连接不活动的检查时间，单位：ms，默认：3s
     */
    private int validateAfterInactivity = 3000;
    /**
     * 最大连接数，默认：150
     */
    private int maxTotal = 150;
    /**
     * 每个路由的最大默认值，默认：100
     */
    private int defaultMaxPerRoute = 100;

    /**
     * 证书类型
     */
    private String type;
    /**
     * 证书文件
     */
    private String keyFile;
    /**
     * 密码
     */
    private String password;
}
