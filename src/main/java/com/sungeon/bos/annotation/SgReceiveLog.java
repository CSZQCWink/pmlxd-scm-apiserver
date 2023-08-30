package com.sungeon.bos.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录接收日志
 *
 * @author 刘国帅
 * @date 2023-1-6
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface SgReceiveLog {

	String name();

	String contentType() default "JSON";

	String sourceColumn() default "docNo";

}
