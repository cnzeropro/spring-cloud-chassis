package org.zero.common.data.util.spring.context;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用SpringContextHelper，即注入SpringContextHelper到容器中
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SpringContextHelperRegistrar.class)
public @interface EnableSpringContextHelper {
}
