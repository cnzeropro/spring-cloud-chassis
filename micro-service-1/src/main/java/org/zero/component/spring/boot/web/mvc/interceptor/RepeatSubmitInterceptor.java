package org.zero.component.spring.boot.web.mvc.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.zero.common.data.model.common.Result;
import org.zero.common.data.util.JacksonUtils;
import org.zero.common.data.util.web.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 防重提交拦截器
 *
 * @author zero
 */
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);
            if (Objects.isNull(repeatSubmit)) {
                return true;
            }
            if (repeatSubmit.value()) {
                return true;
            }
            if (isRepeatSubmit(request, repeatSubmit)) {
                String jsonStr = JacksonUtils.toJsonStr(Result.error(repeatSubmit.message()));
                ResponseUtil.writeErrorJson(response, jsonStr);
                return false;
            }
        }
        return true;
    }

    /**
     * 验证是否重复提交
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit repeatSubmit);
}
