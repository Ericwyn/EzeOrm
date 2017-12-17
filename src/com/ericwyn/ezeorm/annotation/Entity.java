package com.ericwyn.ezeorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类注解，标记的类将成为Entity 实体类
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Entity {
    String table();
}
