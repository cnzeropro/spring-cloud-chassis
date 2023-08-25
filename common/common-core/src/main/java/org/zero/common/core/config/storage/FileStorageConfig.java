package org.zero.common.core.config.storage;

import cn.xuyanwu.spring.file.storage.EnableFileStorage;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://spring-file-storage.xuyanwu.cn/">X Spring File Storage</a>
 * 自动装配：{@link cn.xuyanwu.spring.file.storage.FileStorageAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/10/30
 */
@EnableFileStorage
@Configuration(proxyBeanMethods = false)
public class FileStorageConfig {
}
