package org.zero.demo.spring.boot.web.mvc.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zero
 * @since 2023/6/23
 */
@Slf4j
@RequiredArgsConstructor
public class RefererInterceptor implements HandlerInterceptor {
    private final List<String> allowedAddresses;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CollUtil.isEmpty(allowedAddresses)) {
            return true;
        }
        for (String allowedAddress : allowedAddresses) {
            if ("*".equals(allowedAddress)) {
                return true;
            }

            String origin = request.getHeader("Origin");
            if (StrUtil.contains(allowedAddress, origin)) {
                return true;
            }

            String referer = request.getHeader("Referer");
            if (StrUtil.contains(allowedAddress, referer)) {
                return true;
            }
        }

        throw new NotAcceptableStatusException("非法源访问");
    }
}
