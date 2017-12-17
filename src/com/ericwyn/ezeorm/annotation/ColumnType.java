package com.ericwyn.ezeorm.annotation;

/**
 * <p>
 *     数据库类型枚举类
 * </p>
 * <p>
 *  <code>INT</code>:     <br>
 *      处理 int、Integer、byte、Byte、short、Short、long、Long、Boolean、boolean <br>
 *  <code>BIGINT</code>:  <br>
 *      处理 Long <br>
 *  <code>DOUBLE</code>:  <br>
 *      处理 float、Float、double、Double <br>
 *  <code>DATE</code>:    <br>
 *      处理 Date <br>
 *  <code>TEXT</code>:    <br>
 *      处理 char Char String  <br>
 * </p>
 *

 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */
public enum ColumnType {
    INT,
    BIGINT,
    DATETIME,
    DOUBLE,
    TEXT,
    BOOLEAN,
}
