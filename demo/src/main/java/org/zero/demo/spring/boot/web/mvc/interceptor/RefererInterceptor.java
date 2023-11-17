package org.zero.demo.spring.boot.web.mvc.interceptor;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 访问源拦截
 *
 * @author zero
 * @since 2022/6/23
 */
@Slf4j
@RequiredArgsConstructor
public class RefererInterceptor implements HandlerInterceptor {
    private final List<String> allowedAddresses;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CollectionUtils.isEmpty(allowedAddresses)) {
            return true;
        }

        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        for (String allowedAddress : allowedAddresses) {
            if ("*".equals(allowedAddress)) {
                return true;
            }
            if (CharSequenceUtil.contains(origin, allowedAddress)) {
                return true;
            }
            if (CharSequenceUtil.contains(referer, allowedAddress)) {
                return true;
            }
        }

        log.warn("Prohibited Request  Origin: {}", CharSequenceUtil.isNotBlank(origin) ? origin : referer);
        // 拦截器中抛出的异常能被统一异常处理捕获到
        // throw new NotAcceptableStatusException("非法源访问");
        return false;
    }
}
