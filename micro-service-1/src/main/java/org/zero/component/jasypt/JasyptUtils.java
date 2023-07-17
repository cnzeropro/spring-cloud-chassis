package org.zero.component.jasypt;

import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;
import org.jasypt.encryption.StringEncryptor;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/12/8
 */
@UtilityClass
public class JasyptUtils {
    private static StringEncryptor encryptor;

    static {
        JasyptUtils.encryptor = SpringUtil.getBean(StringEncryptor.class);
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

}
