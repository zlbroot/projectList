package com.zlb.core.annos.dao.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	/**
	 * 表名
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 数据库名称
	 * 
	 * @return
	 */
	String catalog() default "";
}
