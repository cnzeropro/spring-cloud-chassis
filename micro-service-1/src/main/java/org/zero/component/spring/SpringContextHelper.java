package org.zero.component.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.zero.exception.UtilException;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Zero (cnzeropro@qq.com)
 */
@Component
public class SpringContextHelper implements BeanFactoryPostProcessor, ApplicationContextAware {
    /**
     * "@PostConstruct"注解标记的类中，由于ApplicationContext还未加载，导致空指针
     * 因此实现BeanFactoryPostProcessor注入ConfigurableListableBeanFactory实现bean的操作
     */
    private static ConfigurableListableBeanFactory beanFactory;

    /**
     * Spring上下文对象实例
     */
    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        setBf(beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setAc(applicationContext);
    }

    /**
     * 获取 {@link ApplicationContext}
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取 {@link ListableBeanFactory}
     */
    public static ListableBeanFactory getBeanFactory() {
        return Objects.isNull(beanFactory) ? applicationContext : beanFactory;
    }

    /**
     * 获取 {@link ConfigurableListableBeanFactory}
     */
    public static ConfigurableListableBeanFactory getConfigurableBeanFactory() {
        final ConfigurableListableBeanFactory factory;
        if (Objects.nonNull(beanFactory)) {
            factory = beanFactory;
        } else if (applicationContext instanceof ConfigurableApplicationContext) {
            factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        } else {
            throw new UtilException("No ConfigurableListableBeanFactory from context");
        }
        return factory;
    }

    public static Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }

    public static <T> T getBean(Class<T> type) {
        return getBeanFactory().getBean(type);
    }

    public static <T> T getBean(String name, Class<T> type) {
        return getBeanFactory().getBean(name, type);
    }

    /**
     * 通过类型参考返回带泛型参数的Bean
     */
    public static <T> T getBean(TypeReference<T> reference) {
        final ParameterizedType parameterizedType = (ParameterizedType) reference.getType();
        final Class<T> rawType = (Class<T>) parameterizedType.getRawType();
        final Class<?>[] genericTypes = Arrays.stream(parameterizedType.getActualTypeArguments()).map(type -> (Class<?>) type).toArray(Class[]::new);
        final String[] beanNames = getBeanFactory().getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
        return getBean(beanNames[0], rawType);
    }

    /**
     * 获取指定类型对应的所有Bean，包括子类
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    /**
     * 获取指定类型对应的Bean名称，包括子类
     */
    public static String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    /**
     * 获取应用环境
     */
    public static Environment getEnvironment() {
        return Optional.ofNullable(applicationContext).map(ApplicationContext::getEnvironment).orElse(null);
    }

    /**
     * 获取配置文件配置项的值
     */
    public static String getProperty(String key) {
        return Optional.ofNullable(getEnvironment()).map(env -> env.getProperty(key)).orElse(null);
    }

    /**
     * 获取应用程序名称
     */
    public static String getApplicationName() {
        return getProperty("spring.application.name");
    }

    /**
     * 获取当前的环境配置，无配置返回空数组
     */
    public static String[] getActiveProfiles() {
        return Optional.ofNullable(getEnvironment()).map(Environment::getActiveProfiles).orElse(new String[0]);
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     */
    public static String getActiveProfile() {
        final String[] activeProfiles = getActiveProfiles();
        return activeProfiles.length != 0 ? activeProfiles[0] : null;
    }

    /**
     * 动态向Spring注册Bean
     */
    public static <T> void registerBean(String beanName, T bean) {
        final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        factory.autowireBean(bean);
        factory.registerSingleton(beanName, bean);
    }

    /**
     * 注销bean
     * <p>
     * 将Spring中的bean注销，请谨慎使用
     */
    public static void unregisterBean(String beanName) {
        final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        if (factory instanceof DefaultSingletonBeanRegistry) {
            DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) factory;
            registry.destroySingleton(beanName);
        } else {
            throw new UtilException("Can not unregister bean, the factory is not a DefaultSingletonBeanRegistry!");
        }
    }

    /**
     * Sonar 实例方法不应写入“静态”字段 修复
     */
    private static void setBf(ConfigurableListableBeanFactory bf) {
        SpringContextHelper.beanFactory = bf;
    }

    /**
     * Sonar 实例方法不应写入“静态”字段 修复
     */
    private static void setAc(ApplicationContext ac) {
        applicationContext = ac;
    }
}
