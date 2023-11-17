package org.zero.demo.spring.boot.web.mvc.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 防止表单重复提交注解
 *
 * @author zero
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {

    /**
     * 是否允许重复提交，默认不允许
     */
    boolean value() default false;

    /**
     * 间隔时间，小于此时间视为重复提交，默认5000
     */
    int interval() default 5000;

    /**
     * 时间单位，默认毫秒
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;

    /**
     * 提示消息
     */
    String message() default "不允许重复提交，请稍候再试";
}
