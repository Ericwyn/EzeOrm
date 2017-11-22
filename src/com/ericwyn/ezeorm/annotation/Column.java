package com.ericwyn.ezeorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列对象注解
 *
 * Created by Ericwyn on 17-11-20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
//    String name();  //列的名字
    ColumnType type() ;//default ColumnType.TEXT; //列的类型
    boolean notNull() default false;

}
