package org.zero.component.spring.cloud.openfegin.interceptor;

import cn.hutool.core.util.ArrayUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.zero.common.data.util.web.RequestUtil;

import java.util.Optional;

/**
 * @author @author zero
 * @since 2023/3/31
 */
@Slf4j
public class CopyHeaderRequestInterceptor implements RequestInterceptor {
    private static final String[] IGNORED_HEADERS = new String[]{
            "Content-Length",
            "Content-Type",
    };

    @Override
    public void apply(RequestTemplate template) {
        RequestUtil.getHttpServletRequestOpt()
                .ifPresent(request -> Optional.ofNullable(request.getHeaderNames())
                        .ifPresent(headers -> {
                            while (headers.hasMoreElements()) {
                                String name = headers.nextElement();
                                // 跳过指定header
                                if (ArrayUtil.containsIgnoreCase(IGNORED_HEADERS, name)) {
                                    continue;
                                }
                                String header = request.getHeader(name);
                                template.header(name, header);
                                if (log.isTraceEnabled()) {
                                    log.trace(">>> feign header set {}:{}", name, header);
                                }
                            }
                        }));
    }
}
