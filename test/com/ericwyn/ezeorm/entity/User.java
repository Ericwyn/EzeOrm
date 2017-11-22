package com.ericwyn.ezeorm.entity;

import com.ericwyn.ezeorm.annotation.AutoIncrement;
import com.ericwyn.ezeorm.annotation.Column;
import com.ericwyn.ezeorm.annotation.ColumnType;
import com.ericwyn.ezeorm.annotation.Entity;
import com.ericwyn.ezeorm.annotation.PrimaryKey;

import java.util.Date;

import jdk.nashorn.internal.objects.annotations.Constructor;


/**
 *
 * user的实体类
 * Created by Ericwyn on 17-11-20.
 */
@Entity(table = "user3")
public class User {

    @PrimaryKey
    @AutoIncrement
    @Column(type = ColumnType.INT)
    private Long id;

    @Column(type = ColumnType.TEXT,notNull = true)
    private String name;

    @Column(type = ColumnType.INT,notNull = true)
    private int age;

    @Column(type = ColumnType.TEXT,notNull = true)
    private String sex;

    @Column(type = ColumnType.DATETIME,notNull = true)
    private Date timeStamp;

    @Column(type = ColumnType.INT)
    private boolean good;

    private boolean haveMilk;

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

}
