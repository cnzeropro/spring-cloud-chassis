package org.zero.component.spring.boot.web;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.KeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * RestTemplate配置类
 * <p>
 * 相关自动装配参见：
 * {@link org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration}
 * {@link org.springframework.boot.test.autoconfigure.web.client.WebClientRestTemplateAutoConfiguration}
 *
 * @author Zero
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RestTemplateProperties.class)
@AutoConfigureAfter({RestTemplateAutoConfiguration.class})
@RequiredArgsConstructor
public class RestTemplateConfig {
    private final RestTemplateProperties restTemplateProperties;

    /**
     * 无需 @LoadBalanced，当spring cloud中存在负载均衡组件，自动为每个RestTemplate注入负载均衡能力
     * 参见：{@link org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration}
     */
    @Bean
//    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @ConditionalOnBean(HttpComponentsClientHttpRequestFactory.class)
    public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer,
                                                   @Autowired(required = false) RestTemplateCustomizer[] restTemplateCustomizers) {
        RestTemplateBuilder builder = new RestTemplateBuilder(restTemplateCustomizers);
        builder.setConnectTimeout(Duration.ofMillis(restTemplateProperties.getConnectTimeout()));
        builder.setReadTimeout(Duration.ofMillis(restTemplateProperties.getReadTimeout()));
        builder.setBufferRequestBody(true);
        builder.requestFactory(() -> new HttpComponentsClientHttpRequestFactory(createHttpClient()));
        return configurer.configure(builder);
    }

    private HttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(createRequestConfig())
                .setConnectionManager(createConnectionManager())
                .build();
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(restTemplateProperties.getReadTimeout())
                .setConnectTimeout(restTemplateProperties.getConnectTimeout())
                .setConnectionRequestTimeout(restTemplateProperties.getConnectionRequestTimeout())
                .build();
    }

    private HttpClientConnectionManager createConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(createRegistry());
        connectionManager.setMaxTotal(restTemplateProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(restTemplateProperties.getDefaultMaxPerRoute());
        connectionManager.setValidateAfterInactivity(restTemplateProperties.getValidateAfterInactivity());
        return connectionManager;
    }

    private Registry<ConnectionSocketFactory> createRegistry() {
        final ConnectionSocketFactory socketFactory = createConnectionSocketFactory();
        // 支持HTTP、HTTPS
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory());
        // 自定义证书
        if (Objects.nonNull(socketFactory)) {
            registryBuilder.register("https", socketFactory);
        } else {
            registryBuilder.register("https", SSLConnectionSocketFactory.getSocketFactory());
        }
        return registryBuilder.build();
    }

    private ConnectionSocketFactory createConnectionSocketFactory() {
        return Optional.ofNullable(createSslContext(restTemplateProperties.getKeyFile(), restTemplateProperties.getPassword()))
                .map(SSLConnectionSocketFactory::new).orElse(null);
    }

    private SSLContext createSslContext(String keyFile, String password) {
        try {
            SSLContext sslContext = SSLContext.getInstance(restTemplateProperties.getProtocol());
            KeyStore keyStore = KeyUtil.readKeyStore(restTemplateProperties.getType(), FileUtil.newFile(keyFile), password.toCharArray());
            KeyManager[] keyManager = createKeyManager(keyStore, password);
            TrustManager[] trustManager = createTrustManager(keyStore);
            sslContext.init(keyManager, trustManager, RandomUtil.getSecureRandomStrong());
            return sslContext;
        } catch (Exception e) {
            log.warn("Create custom SSLContext error", e);
            return null;
        }
    }

    private KeyManager[] createKeyManager(KeyStore keyStore, String password) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore, password.toCharArray());
        return factory.getKeyManagers();
    }

    private static TrustManager[] createTrustManager(KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore);
        return factory.getTrustManagers();
    }
}
