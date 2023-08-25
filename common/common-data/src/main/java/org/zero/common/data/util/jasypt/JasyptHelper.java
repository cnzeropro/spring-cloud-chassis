package org.zero.common.data.util.jasypt;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.PBEConfig;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/10/3 21:41
 */
public class JasyptHelper {
    private final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

    public JasyptHelper(String password) {
        this(password, "PBEWITHHMACSHA512ANDAES_256");
    }

    public JasyptHelper(String password, String algorithm) {
        SimpleStringPBEConfig config = customConfigurationJasypt(password, algorithm);
        setConfig(config);
    }

    public JasyptHelper(PBEConfig pbeConfig) {
        setConfig(pbeConfig);
    }

    public void setConfig(PBEConfig pbeConfig) {
        encryptor.setConfig(pbeConfig);
    }

    /**
     * 加密
     */
    public String encrypt(String src) {
        return encryptor.encrypt(src);
    }

    /**
     * 解密
     */
    public String decrypt(String src) {
        return encryptor.decrypt(src);
    }

    private SimpleStringPBEConfig customConfigurationJasypt(String password, String algorithm) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        // 加密算法建议：
        // PBEWithMD5AndDES 2.x
        // PBEWITHHMACSHA512ANDAES_256 3.x
        config.setAlgorithm(algorithm);
        config.setKeyObtentionIterations(1000);
        config.setPoolSize(10);
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        // org.jasypt.salt.NoOpIVGenerator 2.x
        // org.jasypt.iv.RandomIvGenerator 3.x
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        return config;
    }
}
