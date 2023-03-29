package org.zero.component.jasypt;

import lombok.experimental.UtilityClass;
import org.jasypt.encryption.StringEncryptor;
import org.zero.component.spring.SpringContextHelper;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/12/8
 */
@UtilityClass
public class JasyptUtil {
    private static StringEncryptor encryptor;

    static {
        JasyptUtil.encryptor = SpringContextHelper.getBean(StringEncryptor.class);
    }

    /**
     * 加密
     *
     * @param src
     * @return
     */
    public String encrypt(String src) {
        return encryptor.encrypt(src);
    }

    /**
     * 解密
     *
     * @param src
     * @return
     */
    public String decrypt(String src) {
        return encryptor.decrypt(src);
    }

}
