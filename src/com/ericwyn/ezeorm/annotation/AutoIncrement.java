package com.ericwyn.ezeorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动递增 注解，标记的属性其所代表的自断将会自增
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoIncrement {

}
