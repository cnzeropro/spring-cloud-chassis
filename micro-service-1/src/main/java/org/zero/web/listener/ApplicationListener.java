package org.zero.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.zero.web.JndiEnhancer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author Zero
 */
@Slf4j
@WebListener
public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        log.info("web 应用（" + servletContext.getContextPath() + "）已启动");
        JndiEnhancer jndiEnhancer = new JndiEnhancer("java:comp/env/jdbc/test");
        servletContext.setAttribute("jndi", jndiEnhancer);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.removeAttribute("jndi");
        log.info("web 应用（" + servletContext.getContextPath() + "）已销毁");
    }
}
