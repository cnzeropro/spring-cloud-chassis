package org.zero.component.spring.cloud.openfegin;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Target;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;
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
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            Optional.ofNullable(headerNames).ifPresent(headers -> {
                while (headers.hasMoreElements()) {
                    String name = headers.nextElement();
                    String header = request.getHeader(name);
                    // 跳过指定header
                    if (ArrayUtil.containsIgnoreCase(IGNORED_HEADERS, name)) {
                        continue;
                    }
                    template.header(name, header);
                    if (log.isTraceEnabled()) {
                        log.trace(">>> feign header set {}:{}", name, header);
                    }
                }
            });
        }
        printLog(template);
    }

    private void printLog(RequestTemplate template) {
        if (log.isDebugEnabled()) {
            Target<?> target = template.feignTarget();
            log.debug("Feign Request:\n\tapp: {}\n\tclass: {}\n\tmethod: {}\n\turl: {}\n\tparam: {}\n",
                    target.name(), target.type().getName(), template.method(),
                    template.url(),
                    getParam(template)
            );
        }
    }

    private String getParam(RequestTemplate template) {
        if (RequestMethod.GET.name().equals(template.method())) {
            return JSONUtil.toJsonStr(template.queries());
        }
        return Objects.isNull(template.body()) ? "" : new String(template.body());
    }
}
