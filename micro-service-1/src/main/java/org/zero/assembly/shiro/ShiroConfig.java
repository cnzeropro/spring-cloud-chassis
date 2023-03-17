package org.zero.assembly.shiro;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/12/1
 */
@Configuration(proxyBeanMethods = false)
public class ShiroConfig {
    @Value("${sys.shiro.excludeUrls:}")
    private List<String> excludeUrls;

    @Bean
    public DefaultWebSecurityManager securityManager(Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDao = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDao.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDao);
        return securityManager;
    }

    /**
     * anon: 无需认证即可访问
     * authc: 必须认证才能访问
     * user: 必须开启“记住我”功能才能用
     * perms: 拥有对某个资源的权限才能访问
     * role: 拥有某个角色权限才能访问
     * port: 请求的端口必须是指定值才可以
     * rest: 请求必须基于 RESTful（POST、PUT、GET、DELETE等等）
     * ssl: 必须是安全的URL请求，协议HTTPS
     * logout: 表示退出，当有该请求时，会执行退出，清除用户信息及角色和权限信息
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();

        // 自定义过滤器
        Map<String, Filter> filterMap = new LinkedHashMap<>(16);
//        filterMap.put("jwt", new JwtFilter());

        // 自定义拦截器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(16);
        // 放开的URL
        excludeUrls.forEach(excludeUrl -> filterChainDefinitionMap.put(excludeUrl, "anon"));

        // 自定义访问权限
        // 从上往下一一匹配，一旦匹配成功后续将不再匹配处理
        filterChainDefinitionMap.put("/**/logout", "logout");
        filterChainDefinitionMap.put("/**/login", "anon");
        filterChainDefinitionMap.put("/actuator/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger**/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/ws/**", "anon");
        filterChainDefinitionMap.put("/websocket/**", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/**/*.svg", "anon");
        filterChainDefinitionMap.put("/**/*.pdf", "anon");
        filterChainDefinitionMap.put("/**/*.jpg", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        filterChainDefinitionMap.put("/**/*.ico", "anon");
        filterChainDefinitionMap.put("/**/*.jsp", "anon");
        filterChainDefinitionMap.put("/**/*.ftl", "anon");
        filterChainDefinitionMap.put("/**/*.ttf", "anon");
        filterChainDefinitionMap.put("/**/*.woff", "anon");
        filterChainDefinitionMap.put("/**/*.woff2", "anon");
        // 拦截全部，使用自定义过滤器处理，一般放在最下边
        filterChainDefinitionMap.put("/**", "jwt");

        // 设置安全管理器
        bean.setSecurityManager(securityManager);
        // 设置过滤器
        bean.setFilters(filterMap);
        // 设置拦截器链
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    @Bean
    public SubjectFactory webSubjectFactory() {
        return new DefaultWebSubjectFactory() {
            @Override
            public Subject createSubject(SubjectContext context) {
                // 不创建session
                context.setSessionCreationEnabled(false);
                return super.createSubject(context);
            }
        };
    }
}
