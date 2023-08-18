package org.zero.component.spring.cloud.openfegin.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.zero.common.data.util.web.RequestUtil;

/**
 * @author @author zero
 * @since 2023/3/31
 */
@Slf4j
public class TokenRequestInterceptor implements RequestInterceptor {
    private static final String ACCESS_TOKEN = "X-Access-Token";

    @Override
    public void apply(RequestTemplate template) {
        RequestUtil.getHttpServletRequestOpt()
                // token透传
                .ifPresent(request -> template.header(ACCESS_TOKEN, request.getHeader(ACCESS_TOKEN)));
    }
}
