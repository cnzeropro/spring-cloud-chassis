package org.zero.component.spring.cloud.openfegin;

import feign.Client;
import feign.Feign;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.zero.common.data.util.net.ssl.SslUtil;
import org.zero.component.spring.boot.web.SslProperties;
import org.zero.component.spring.cloud.openfegin.interceptor.CopyHeaderRequestInterceptor;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.KeyStore;
import java.util.Objects;

/**
 * 注意：如果不想该配置全局生效，就不要添加 @Configuration 注解
 * 使用：@FeignClient(configuration = CustomFeignClientConfiguration.class)
 * 优先级：细粒度属性配置 > 细粒度代码配置 > 全局属性配置 > 全局代码配置
 * <p>
 * Spring Feign 全局配置：{@link org.springframework.cloud.openfeign.FeignClientsConfiguration}
 *
 * @author zero
 * @since 2021/2/14
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(SslProperties.class)
public class CustomFeignClientConfiguration {
    private final SslProperties sslProperties;

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder().client(new Client.Default(getSslSocketFactory(), SslUtil.DEFAULT_HOSTNAME_VERIFIER));
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default();
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new CopyHeaderRequestInterceptor();
    }

    private SSLSocketFactory getSslSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            // 构建 KeyManager
            char[] password = sslProperties.getPassword().toCharArray();
            KeyStore keyStore = SslUtil.createKeyStore(sslProperties.getType(), sslProperties.getKeyFile(), password);
            KeyManager[] keyManagers = SslUtil.createKeyManager(keyStore, password);

            // 构建 TrustManager
            char[] trustPassword = sslProperties.getTrustPassword().toCharArray();
            KeyStore trustKeyStore = SslUtil.createKeyStore(sslProperties.getType(), sslProperties.getTrustFile(), trustPassword);
            TrustManager[] trustManagers = SslUtil.createTrustManager(trustKeyStore);

            // 构建 SSLContext
            SSLContext sslContext = SslUtil.createSslContext(sslProperties.getProtocol(), keyManagers, trustManagers);

            sslSocketFactory = SslUtil.createSslSocketFactory(sslContext);
        } catch (Exception e) {
            log.warn("Create custom SSLSocketFactory error", e);
        }
        return Objects.isNull(sslSocketFactory) ? SslUtil.DEFAULT_SSL_SOCKET_FACTORY : sslSocketFactory;
    }
}
