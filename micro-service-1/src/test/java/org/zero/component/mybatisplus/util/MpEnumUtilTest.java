package org.zero.component.mybatisplus.util;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.zero.component.mybatisplus.constant.StatusEnum;
import org.zero.component.spring.boot.web.mvc.conversion.StrToEnumConverterFactory;

import java.lang.reflect.Method;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2023/1/5
 */
class MpEnumUtilTest {
    @Test
    void test() {
        Method method = MpEnumUtil.getMethod(StatusEnum.class);
        System.out.println(method);
        StrToEnumConverterFactory converterFactory = new StrToEnumConverterFactory();
        Converter<String, StatusEnum> converter = converterFactory.getConverter(StatusEnum.class);
        StatusEnum statusEnum = converter.convert("1");
        System.out.println(statusEnum);
    }
}