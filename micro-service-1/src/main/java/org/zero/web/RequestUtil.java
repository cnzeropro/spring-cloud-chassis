package org.zero.web;

import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/23
 */
@UtilityClass
public class RequestUtil {
    /**
     * 获取当前HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    public static String getOrigin(HttpServletRequest request) {
        return request.getHeader("Origin");
    }

    public static MediaType getMediaType(ServletRequest request) {
        return MediaType.valueOf(request.getContentType());
    }
}
