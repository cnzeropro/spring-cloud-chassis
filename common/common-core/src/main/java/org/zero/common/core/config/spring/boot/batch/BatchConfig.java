package org.zero.common.core.config.spring.boot.batch;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;

/**
 * @author zero
 * @since 2021/10/25
 */
@EnableBatchProcessing
@Configuration(proxyBeanMethods = false)
public class BatchConfig extends DefaultBatchConfigurer {
    @Override
    protected JobLauncher createJobLauncher() throws Exception {
        // 此处可自定义JobLauncher
        return super.createJobLauncher();
    }
}
