package org.zero.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Zero
 */
public abstract class BaseServlet extends HttpServlet {
    private static final String ACTION = "action";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取隐藏域动作
        String action = request.getParameter(ACTION);

        try {
            // 反射获取与动作对应的子类方法
            Method method = getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);

            // 忽略private关键字
            // method.setAccessible(true);

            // 调用子类方法
            method.invoke(getClass().getDeclaredConstructor().newInstance(), request, response);
        } catch (Exception e) {
            // 代理情况下拿取真实错误信息
            String msg = e.getMessage();
            Throwable cause = e.getCause();
            if (Objects.nonNull(cause)) {
                msg = cause.getMessage();
            }
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }
}
