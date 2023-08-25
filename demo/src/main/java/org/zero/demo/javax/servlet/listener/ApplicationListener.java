package org.zero.demo.javax.servlet.listener;

import lombok.extern.slf4j.Slf4j;
import org.zero.common.data.util.javax.web.JndiHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author Zero
 */
@Slf4j
@WebListener("Application Listener")
public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        log.info("The web app ({}) is initialized", servletContext.getContextPath());
        JndiHelper jndiHelper = new JndiHelper("java:comp/env/jdbc/test");
        servletContext.setAttribute("jndi", jndiHelper);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.removeAttribute("jndi");
        log.info("The web app ({}) has been destroyed", servletContext.getContextPath());
    }
}
