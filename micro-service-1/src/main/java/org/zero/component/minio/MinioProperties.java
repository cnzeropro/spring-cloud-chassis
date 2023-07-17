package org.zero.component.minio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Zero
 * @date 2021/10/20 13:36
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    /**
     * 端点
     */
    private String endpoint;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 数据桶，如果配置则系统启动时自动创建，反之则反
     */
    private List<String> buckets;
}
