package com.ericwyn.ezeorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库字段注解，标记的属性将成为数据表当中的字段
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
//    String name();  //列的名字
    ColumnType type() ;//default ColumnType.TEXT; //列的类型
    boolean notNull() default false;

}
