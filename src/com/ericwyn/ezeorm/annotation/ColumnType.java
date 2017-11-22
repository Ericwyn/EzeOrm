package com.ericwyn.ezeorm.annotation;

/**
 *
 * 对象关系映射表
 *
 * INT:     处理 int、Integer、byte、Byte、short、Short、long、Long、Boolean、boolean
 * BIGINT:  处理 Long
 * DOUBLE:  处理 float、Float、double、Double
 * DATE:    处理 Date
 * TEXT:    处理 char Char String
 *
 * Created by Ericwyn on 17-11-20.
 */
public enum ColumnType {
    INT,
    BIGINT,
    DATETIME,
    DOUBLE,
    TEXT,
    BOOLEAN,
}
