package org.zero.common.core.config.spring.boot.web.client;

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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.zero.common.data.util.javax.net.SslUtil;

import javax.annotation.Resource;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyStore;
import java.util.Objects;

/**
 * 自动装配：{@link org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration}
 *
 * @author Zero
 */
@Slf4j
@EnableConfigurationProperties({HttpProperties.class, SslProperties.class})
@AutoConfigureAfter({RestTemplateAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {
    @Resource
    private HttpProperties httpProperties;
    @Resource
    private SslProperties sslProperties;

    /**
     * 无需 @LoadBalanced，当 spring cloud 中存在负载均衡组件，自动为每个 RestTemplate 注入负载均衡能力。
     * 详情参见：{@link org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration}
     */
    @Bean
//    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @ConditionalOnBean(HttpComponentsClientHttpRequestFactory.class)
    public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.setConnectTimeout(httpProperties.getConnectTimeout());
        builder.setReadTimeout(httpProperties.getReadTimeout());
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
                .setSocketTimeout((int) httpProperties.getReadTimeout().toMillis())
                .setConnectTimeout((int) httpProperties.getConnectTimeout().toMillis())
                .setConnectionRequestTimeout((int) httpProperties.getConnectionRequestTimeout().toMillis())
                .build();
    }

    private HttpClientConnectionManager createConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(createRegistry());
        connectionManager.setMaxTotal(httpProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(httpProperties.getDefaultMaxPerRoute());
        connectionManager.setValidateAfterInactivity((int) httpProperties.getValidateAfterInactivity().toMillis());
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
        SSLContext sslContext = null;

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
            sslContext = SslUtil.createSslContext(sslProperties.getProtocol(), keyManagers, trustManagers);
        } catch (Exception e) {
            log.warn("Create custom SSLContext error", e);
        }

        // 构建 SSLConnectionSocketFactory
        return Objects.isNull(sslContext) ? null : new SSLConnectionSocketFactory(sslContext);
    }
}
