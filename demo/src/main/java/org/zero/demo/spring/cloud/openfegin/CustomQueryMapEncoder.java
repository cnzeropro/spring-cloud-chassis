package org.zero.demo.spring.cloud.openfegin;

import feign.QueryMapEncoder;
import lombok.extern.slf4j.Slf4j;
import org.zero.common.data.util.java.bean.BeanMapUtil;

import java.util.Map;

/**
 * 使用方式：
 * <pre>
 *    @Bean
 *    public Feign.Builder feignBuilder() {
 * 		return Feign.builder().queryMapEncoder(new CustomQueryMapEncoder());
 *    }
 * </pre>
 *
 * @author zero
 * @since 2021/2/14
 */
@Slf4j
public class CustomQueryMapEncoder implements QueryMapEncoder {
    private static final String BEAN_BASE_PACKAGE = "org.zero.common.data.model";

    @Override
    public Map<String, Object> encode(Object object) {
        return BeanMapUtil.encodeIn(BEAN_BASE_PACKAGE, object);
    }
}
