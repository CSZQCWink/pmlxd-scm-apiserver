package com.sungeon.bos.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录发送日志
 *
 * @author 刘国帅
 * @date 2023-1-6
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface SgSendLog {

	String name() default "";

	String contentType() default "JSON";

	String successName() default "";

	String successValue() default "";

}
