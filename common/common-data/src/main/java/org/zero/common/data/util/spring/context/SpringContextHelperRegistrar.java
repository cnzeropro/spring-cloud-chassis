package org.zero.common.data.util.spring.context;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author zero
 * @since 2021/8/17
 */
public class SpringContextHelperRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        String name = SpringContextHelper.class.getName();
        if (!registry.containsBeanDefinition(name)) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SpringContextHelper.class);
            registry.registerBeanDefinition(name, builder.getBeanDefinition());
        }
    }
}
