package org.zero.component.spring.cloud.openfegin.interceptor;

import cn.hutool.json.JSONUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Target;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author @author zero
 * @since 2023/3/31
 */
@Slf4j
public class LogRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        if (log.isDebugEnabled()) {
            Target<?> target = template.feignTarget();
            log.debug("Feign Request:\n\tapp: {}\n\tclass: {}\n\tmethod: {}\n\turl: {}\n\tparam: {}\n\tbody: {}\n",
                    target.name(),
                    target.type().getName(),
                    template.method(),
                    template.url(),
                    JSONUtil.toJsonStr(template.queries()),
                    Optional.ofNullable(template.body()).map(String::new).orElse("")
            );
        }
    }
}
