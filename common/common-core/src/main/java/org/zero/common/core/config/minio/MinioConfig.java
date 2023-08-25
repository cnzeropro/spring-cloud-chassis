package org.zero.common.core.config.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 官网：<a href="http://minio.org.cn/">Minio</a>
 *
 * @author Zero
 * @date 2021/10/20 13:36
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "minio", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MinioProperties.class)
@Configuration
public class MinioConfig {
    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    @PostConstruct
    public void initBucket() throws Exception {
        List<String> buckets = minioProperties.getBuckets();
        if (!CollectionUtils.isEmpty(buckets)) {
            MinioClient minioClient = minioClient();
            for (String bucket : buckets) {
                if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                    log.info("Bucket already exists: {}", bucket);
                } else {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                    log.info("Bucket has been made: {}", bucket);
                }
            }
        }
    }
}
